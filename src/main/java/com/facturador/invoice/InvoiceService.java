package com.facturador.invoice;

import com.facturador.hacienda.HaciendaClient;
import com.facturador.hacienda.HaciendaTokenService;
import com.facturador.hacienda.dto.HaciendaStatusResponse;
import com.facturador.hacienda.dto.HaciendaSubmissionResponse;
import com.facturador.invoice.dto.CreateInvoiceRequest;
import com.facturador.invoice.dto.InvoiceResponse;
import com.facturador.storage.FileUtils;
import com.facturador.tenant.Tenant;
import com.facturador.tenant.TenantRepository;
import com.facturador.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final HaciendaResponseRepository haciendaResponseRepository;
    private final ClaveGenerator claveGenerator;
    private final com.facturador.xml.XmlInvoiceGenerator xmlInvoiceGenerator;
    private final com.facturador.xml.XmlSigner xmlSigner;
    private final com.facturador.storage.DocumentStorageService documentStorageService;
    private final HaciendaClient haciendaClient;
    private final HaciendaTokenService haciendaTokenService;
    private final TenantRepository tenantRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          HaciendaResponseRepository haciendaResponseRepository,
                          ClaveGenerator claveGenerator,
                          com.facturador.xml.XmlInvoiceGenerator xmlInvoiceGenerator,
                          com.facturador.xml.XmlSigner xmlSigner,
                          com.facturador.storage.DocumentStorageService documentStorageService,
                          HaciendaClient haciendaClient,
                          HaciendaTokenService haciendaTokenService,
                          TenantRepository tenantRepository) {
        this.invoiceRepository = invoiceRepository;
        this.haciendaResponseRepository = haciendaResponseRepository;
        this.claveGenerator = claveGenerator;
        this.xmlInvoiceGenerator = xmlInvoiceGenerator;
        this.xmlSigner = xmlSigner;
        this.documentStorageService = documentStorageService;
        this.haciendaClient = haciendaClient;
        this.haciendaTokenService = haciendaTokenService;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Tenant tenant = requireTenant();
        Invoice invoice = new Invoice();
        invoice.setTenant(tenant);
        long sequence = tenant.getNextInvoiceSequence();
        String clave = claveGenerator.generateClave(tenant, request.documentType(), sequence);
        invoice.setClave(clave);
        invoice.setConsecutive(String.format("%08d", sequence));
        invoice.setDocumentType(request.documentType());
        invoice.setCurrency(request.currency());
        invoice.setTotal(request.total());
        invoice.setCustomerName(request.customerName());
        invoice.setStatus(Invoice.Status.CREATED.name());

        request.items().forEach(item -> {
            InvoiceItem entity = new InvoiceItem();
            entity.setInvoice(invoice);
            entity.setTenant(tenant);
            entity.setLineNumber(item.lineNumber());
            entity.setDescription(item.description());
            entity.setQuantity(item.quantity());
            entity.setUnitPrice(item.unitPrice());
            entity.setSubtotal(item.subtotal());
            entity.setTaxAmount(item.taxAmount());
            invoice.getItems().add(entity);
        });

        if (request.taxes() != null) {
            request.taxes().forEach(tax -> {
                InvoiceTax invoiceTax = new InvoiceTax();
                invoiceTax.setInvoice(invoice);
                invoiceTax.setTenant(tenant);
                invoiceTax.setTaxType(tax.taxType());
                invoiceTax.setTaxRate(tax.taxRate());
                invoiceTax.setTaxAmount(tax.taxAmount());
                invoice.getTaxes().add(invoiceTax);
            });
        }

        tenant.setNextInvoiceSequence(sequence + 1);
        tenantRepository.save(tenant);

        String unsignedXml = xmlInvoiceGenerator.generate(invoice);
        String signedXml = xmlSigner.sign(tenant, unsignedXml);
        String path = documentStorageService.storeSignedXml(tenant.getId(), clave, signedXml);
        invoice.setXmlPath(path);
        invoice.setStatus(Invoice.Status.SENT.name());
        invoice.setUpdatedAt(OffsetDateTime.now());

        Invoice persisted = invoiceRepository.save(invoice);

        String token = haciendaTokenService.getToken(tenant);
        HaciendaSubmissionResponse submissionResponse = haciendaClient.sendInvoice(tenant, signedXml, token);
        persisted.setLocationHeader(submissionResponse.locationHeader());
        invoiceRepository.save(persisted);

        HaciendaResponse response = new HaciendaResponse();
        response.setInvoice(persisted);
        response.setTenant(tenant);
        response.setStatus(submissionResponse.status());
        response.setResponsePayload("Submitted to Hacienda");
        haciendaResponseRepository.save(response);

        return new InvoiceResponse(persisted.getClave(), persisted.getStatus(), persisted.getLocationHeader(), persisted.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(String clave) {
        Tenant tenant = requireTenant();
        Invoice invoice = invoiceRepository.findByClaveAndTenantId(clave, tenant.getId())
            .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        return new InvoiceResponse(invoice.getClave(), invoice.getStatus(), invoice.getLocationHeader(), invoice.getCreatedAt());
    }

    @Transactional
    public InvoiceResponse retryInvoice(String clave) {
        Tenant tenant = requireTenant();
        Invoice invoice = invoiceRepository.findByClaveAndTenantId(clave, tenant.getId())
            .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        String xml = com.facturador.storage.FileUtils.readFile(invoice.getXmlPath());
        String token = haciendaTokenService.getToken(tenant);
        HaciendaSubmissionResponse submissionResponse = haciendaClient.sendInvoice(tenant, xml, token);
        invoice.setStatus(Invoice.Status.SENT.name());
        invoice.setLocationHeader(submissionResponse.locationHeader());
        invoice.setUpdatedAt(OffsetDateTime.now());
        invoiceRepository.save(invoice);

        HaciendaResponse response = new HaciendaResponse();
        response.setInvoice(invoice);
        response.setTenant(tenant);
        response.setStatus(submissionResponse.status());
        response.setResponsePayload("Retry submitted");
        haciendaResponseRepository.save(response);
        return new InvoiceResponse(invoice.getClave(), invoice.getStatus(), invoice.getLocationHeader(), invoice.getCreatedAt());
    }

    @Transactional
    public void refreshStatus(Invoice invoice) {
        Tenant tenant = invoice.getTenant();
        String token = haciendaTokenService.getToken(tenant);
        HaciendaStatusResponse statusResponse = haciendaClient.checkStatus(tenant, invoice.getClave(), token);
        invoice.setStatus(statusResponse.status());
        invoice.setUpdatedAt(OffsetDateTime.now());
        invoiceRepository.save(invoice);

        HaciendaResponse response = new HaciendaResponse();
        response.setInvoice(invoice);
        response.setTenant(tenant);
        response.setStatus(statusResponse.status());
        response.setResponsePayload(statusResponse.responsePayload());
        haciendaResponseRepository.save(response);
    }

    private Tenant requireTenant() {
        Tenant contextTenant = TenantContext.getTenant();
        if (contextTenant == null) {
            throw new IllegalStateException("Tenant context not available");
        }
        return tenantRepository.findById(contextTenant.getId())
            .orElseThrow(() -> new IllegalStateException("Tenant not found"));
    }
}
