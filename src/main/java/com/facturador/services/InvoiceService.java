package com.facturador.services;

import com.facturador.dto.invoice.CreateInvoiceRequest;
import com.facturador.dto.invoice.InvoiceItemRequest;
import com.facturador.dto.invoice.InvoiceResponse;
import com.facturador.dto.invoice.InvoiceTaxRequest;
import com.facturador.hacienda.HaciendaClient;
import com.facturador.hacienda.dto.HaciendaStatusResponse;
import com.facturador.hacienda.dto.HaciendaSubmissionResponse;
import com.facturador.models.invoice.HaciendaResponse;
import com.facturador.models.invoice.Invoice;
import com.facturador.models.invoice.InvoiceItem;
import com.facturador.models.invoice.InvoiceTax;
import com.facturador.models.tenant.Tenant;
import com.facturador.repositories.HaciendaResponseRepository;
import com.facturador.repositories.InvoiceRepository;
import com.facturador.repositories.TenantRepository;
import com.facturador.storage.DocumentStorageService;
import com.facturador.storage.FileUtils;
import com.facturador.tenant.context.TenantContext;
import com.facturador.utils.ClaveGenerator;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final HaciendaResponseRepository haciendaResponseRepository;
    private final ClaveGenerator claveGenerator;
    private final com.facturador.xml.XmlInvoiceGenerator xmlInvoiceGenerator;
    private final com.facturador.xml.XmlSigner xmlSigner;
    private final DocumentStorageService documentStorageService;
    private final HaciendaClient haciendaClient;
    private final HaciendaTokenService haciendaTokenService;
    private final TenantRepository tenantRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          HaciendaResponseRepository haciendaResponseRepository,
                          ClaveGenerator claveGenerator,
                          com.facturador.xml.XmlInvoiceGenerator xmlInvoiceGenerator,
                          com.facturador.xml.XmlSigner xmlSigner,
                          DocumentStorageService documentStorageService,
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

        OffsetDateTime issueDate = request.issueDate();
        invoice.setIssueDate(issueDate);
        long sequence = tenant.getNextInvoiceSequence();
        String consecutive = claveGenerator.generateConsecutive(tenant, request.documentType(), sequence);
        invoice.setConsecutive(consecutive);
        String clave = claveGenerator.generateClave(tenant, request.documentType(), sequence, issueDate);
        invoice.setClave(clave);
        invoice.setActivityCode(request.activityCode());
        invoice.setDocumentType(request.documentType());
        invoice.setSaleCondition(request.saleCondition());
        invoice.setCreditTerm(request.creditTerm());
        invoice.setCurrency(request.currency());
        invoice.setExchangeRate(request.exchangeRate());
        invoice.setPaymentMethods(new ArrayList<>(request.paymentMethods()));
        invoice.setTotal(request.summary().totalVoucher());

        invoice.setCustomerName(request.receiver().name());
        invoice.setReceiverIdentificationType(request.receiver().identificationType());
        invoice.setReceiverIdentificationNumber(request.receiver().identificationNumber());
        invoice.setReceiverCommercialName(request.receiver().commercialName());
        invoice.setReceiverProvince(request.receiver().location().province());
        invoice.setReceiverCanton(request.receiver().location().canton());
        invoice.setReceiverDistrict(request.receiver().location().district());
        invoice.setReceiverNeighborhood(request.receiver().location().neighborhood());
        invoice.setReceiverOtherSigns(request.receiver().location().otherSigns());
        if (request.receiver().phone() != null) {
            invoice.setReceiverPhoneCountry(request.receiver().phone().countryCode());
            invoice.setReceiverPhoneNumber(request.receiver().phone().number());
        }
        invoice.setReceiverEmail(request.receiver().email());

        invoice.setSummaryTotalTaxedServices(request.summary().totalTaxedServices());
        invoice.setSummaryTotalExemptServices(request.summary().totalExemptServices());
        invoice.setSummaryTotalTaxedGoods(request.summary().totalTaxedGoods());
        invoice.setSummaryTotalExemptGoods(request.summary().totalExemptGoods());
        invoice.setSummaryTotalTaxed(request.summary().totalTaxed());
        invoice.setSummaryTotalExempt(request.summary().totalExempt());
        invoice.setSummaryTotalSale(request.summary().totalSale());
        invoice.setSummaryTotalDiscounts(request.summary().totalDiscounts());
        invoice.setSummaryTotalNetSale(request.summary().totalNetSale());
        invoice.setSummaryTotalTax(request.summary().totalTax());
        invoice.setSummaryTotalIvaRefund(request.summary().totalIVARefund());
        invoice.setSummaryTotalOtherCharges(request.summary().totalOtherCharges());
        invoice.setSummaryTotalVoucher(request.summary().totalVoucher());

        invoice.setStatus(Invoice.Status.CREATED.name());

        List<InvoiceItem> items = new ArrayList<>();
        for (InvoiceItemRequest itemRequest : request.items()) {
            InvoiceItem entity = new InvoiceItem();
            entity.setInvoice(invoice);
            entity.setTenant(tenant);
            entity.setLineNumber(itemRequest.lineNumber());
            entity.setCommercialCode(itemRequest.commercialCode());
            entity.setDescription(itemRequest.description());
            entity.setQuantity(itemRequest.quantity());
            entity.setMeasurementUnit(itemRequest.measurementUnit());
            entity.setCommercialUnit(itemRequest.commercialUnit());
            entity.setUnitPrice(itemRequest.unitPrice());
            entity.setTotalAmount(itemRequest.totalAmount());
            entity.setDiscountAmount(itemRequest.discountAmount());
            entity.setDiscountReason(itemRequest.discountReason());
            entity.setSubtotal(itemRequest.subtotal());
            entity.setTaxableBase(itemRequest.taxableBase());
            entity.setTotalLineAmount(itemRequest.totalLineAmount());

            if (itemRequest.taxes() != null) {
                List<InvoiceTax> taxes = new ArrayList<>();
                for (InvoiceTaxRequest taxRequest : itemRequest.taxes()) {
                    InvoiceTax invoiceTax = new InvoiceTax();
                    invoiceTax.setInvoiceItem(entity);
                    invoiceTax.setTenant(tenant);
                    invoiceTax.setTaxCode(taxRequest.taxCode());
                    invoiceTax.setRateCode(taxRequest.rateCode());
                    invoiceTax.setTaxRate(taxRequest.taxRate());
                    invoiceTax.setFactorIVA(taxRequest.factorIVA());
                    invoiceTax.setTaxableBase(taxRequest.taxableBase());
                    invoiceTax.setTaxAmount(taxRequest.taxAmount());
                    taxes.add(invoiceTax);
                }
                entity.setTaxes(taxes);
            }
            items.add(entity);
        }
        invoice.setItems(items);

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
        String xml = FileUtils.readFile(invoice.getXmlPath());
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
