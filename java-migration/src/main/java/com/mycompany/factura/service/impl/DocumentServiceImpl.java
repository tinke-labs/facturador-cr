package com.mycompany.factura.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.factura.config.properties.FacturaProperties;
import com.mycompany.factura.models.dto.DocumentRequest;
import com.mycompany.factura.models.dto.ReceiverMessageRequest;
import com.mycompany.factura.models.entities.ElectronicDocument;
import com.mycompany.factura.models.entities.LineItem;
import com.mycompany.factura.models.entities.ReceiverMessage;
import com.mycompany.factura.models.entities.RetryLog;
import com.mycompany.factura.models.entities.SubmissionHistory;
import com.mycompany.factura.models.entities.HaciendaStatus;
import com.mycompany.factura.models.entities.Tax;
import com.mycompany.factura.models.entities.Totals;
import com.mycompany.factura.repository.DocumentRepository;
import com.mycompany.factura.repository.HaciendaStatusRepository;
import com.mycompany.factura.repository.ReceiverMessageRepository;
import com.mycompany.factura.repository.RetryLogRepository;
import com.mycompany.factura.repository.SubmissionHistoryRepository;
import com.mycompany.factura.service.DocumentService;
import com.mycompany.factura.utils.Base64Util;
import com.mycompany.factura.utils.HaciendaClient;
import com.mycompany.factura.utils.UuidGenerator;
import com.mycompany.factura.utils.queue.RetryPublisher;
import com.mycompany.factura.utils.xml.CertificateLoader;
import com.mycompany.factura.utils.xml.XmlSignerUtil;
import com.mycompany.factura.utils.xml.XsdValidatorUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final SubmissionHistoryRepository submissionHistoryRepository;
    private final HaciendaStatusRepository haciendaStatusRepository;
    private final RetryLogRepository retryLogRepository;
    private final ReceiverMessageRepository receiverMessageRepository;
    private final XmlSignerUtil xmlSignerUtil;
    private final XsdValidatorUtil xsdValidatorUtil;
    private final HaciendaClient haciendaClient;
    private final RetryPublisher retryPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               SubmissionHistoryRepository submissionHistoryRepository,
                               HaciendaStatusRepository haciendaStatusRepository,
                               RetryLogRepository retryLogRepository,
                               ReceiverMessageRepository receiverMessageRepository,
                               FacturaProperties facturaProperties,
                               HaciendaClient haciendaClient,
                               RetryPublisher retryPublisher,
                               CertificateLoader certificateLoader) {
        this.documentRepository = documentRepository;
        this.submissionHistoryRepository = submissionHistoryRepository;
        this.haciendaStatusRepository = haciendaStatusRepository;
        this.retryLogRepository = retryLogRepository;
        this.receiverMessageRepository = receiverMessageRepository;
        this.haciendaClient = haciendaClient;
        this.retryPublisher = retryPublisher;
        this.xmlSignerUtil = new XmlSignerUtil(certificateLoader);
        this.xsdValidatorUtil = new XsdValidatorUtil(new File(facturaProperties.getXsdPath()));
    }

    @Override
    @Transactional
    public ElectronicDocument createDocument(DocumentRequest request) {
        ElectronicDocument document = new ElectronicDocument();
        document.setKey(UuidGenerator.generateKey());
        document.setConsecutive(generateConsecutive(request.getType()));
        document.setType(request.getType());
        document.setIssuedAt(LocalDateTime.now());
        document.setIssuer(request.getIssuer());
        document.setReceiver(request.getReceiver());
        document.setTotals(request.getTotals());
        List<LineItem> lineItems = request.getLineItems() != null ? new ArrayList<>(request.getLineItems()) : new ArrayList<>();
        lineItems.forEach(item -> item.setDocument(document));
        document.setLineItems(lineItems);
        document.setHaciendaStatus("PENDIENTE");
        return documentRepository.save(document);
    }

    @Override
    public String generateXml(ElectronicDocument document) {
        StringBuilder builder = new StringBuilder();
        String rootName = switch (document.getType()) {
            case FACTURA -> "FacturaElectronica";
            case NOTA_CREDITO -> "NotaCreditoElectronica";
            case NOTA_DEBITO -> "NotaDebitoElectronica";
            case TIQUETE -> "TiqueteElectronico";
            case MENSAJE_RECEPTOR -> "MensajeReceptor";
            case FACTURA_EXPORTACION -> "FacturaElectronicaExportacion";
            case FACTURA_COMPRA -> "FacturaElectronicaCompra";
            case RECIBO_ELECTRONICO_PAGO -> "ReciboElectronico";
        };
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        String namespace = " xmlns=\"https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4 " +
                rootName + ".xsd\"";
        builder.append('<').append(rootName).append(namespace).append('>');
        builder.append("<Clave>").append(document.getKey()).append("</Clave>");
        builder.append("<CodigoActividad>001</CodigoActividad>");
        builder.append("<Consecutivo>").append(document.getConsecutive()).append("</Consecutivo>");
        builder.append("<FechaEmision>").append(document.getIssuedAt()).append("</FechaEmision>");
        builder.append("<Emisor>");
        builder.append("<Nombre>").append(document.getIssuer().getName()).append("</Nombre>");
        builder.append("<Identificacion><Tipo>").append(document.getIssuer().getIdentificationType()).append("</Tipo><Numero>")
                .append(document.getIssuer().getIdentification()).append("</Numero></Identificacion>");
        builder.append("<CorreoElectronico>").append(document.getIssuer().getEmail()).append("</CorreoElectronico>");
        builder.append("</Emisor>");
        builder.append("<Receptor>");
        builder.append("<Nombre>").append(document.getReceiver().getName()).append("</Nombre>");
        builder.append("<Identificacion><Tipo>").append(document.getReceiver().getIdentificationType()).append("</Tipo><Numero>")
                .append(document.getReceiver().getIdentification()).append("</Numero></Identificacion>");
        builder.append("<CorreoElectronico>").append(document.getReceiver().getEmail()).append("</CorreoElectronico>");
        builder.append("</Receptor>");
        builder.append("<ResumenFactura>");
        Totals totals = document.getTotals();
        builder.append("<TotalVenta>").append(totals.getSaleTotal()).append("</TotalVenta>");
        builder.append("<TotalVentaNeta>").append(totals.getNetSaleTotal()).append("</TotalVentaNeta>");
        builder.append("<TotalImpuesto>").append(totals.getTaxTotal()).append("</TotalImpuesto>");
        builder.append("<TotalComprobante>").append(totals.getDocumentTotal()).append("</TotalComprobante>");
        builder.append("</ResumenFactura>");
        builder.append("<DetalleServicio>");
        for (LineItem lineItem : document.getLineItems()) {
            builder.append("<LineaDetalle>");
            builder.append("<NumeroLinea>").append(lineItem.getLineNumber()).append("</NumeroLinea>");
            builder.append("<Codigo>").append(lineItem.getCode()).append("</Codigo>");
            builder.append("<Cantidad>").append(lineItem.getQuantity()).append("</Cantidad>");
            builder.append("<UnidadMedida>Sp</UnidadMedida>");
            builder.append("<Detalle>").append(lineItem.getDescription()).append("</Detalle>");
            builder.append("<PrecioUnitario>").append(lineItem.getUnitPrice()).append("</PrecioUnitario>");
            builder.append("<MontoTotal>").append(lineItem.getSubtotal()).append("</MontoTotal>");
            if (lineItem.getDiscount() != null) {
                builder.append("<Descuento>").append(lineItem.getDiscount()).append("</Descuento>");
            }
            Tax tax = lineItem.getTax();
            if (tax != null) {
                builder.append("<Impuesto><Codigo>").append(tax.getCode())
                        .append("</Codigo><Tarifa>").append(tax.getRate())
                        .append("</Tarifa><Monto>").append(tax.getAmount())
                        .append("</Monto></Impuesto>");
            }
            builder.append("<MontoTotalLinea>").append(lineItem.getLineTotal()).append("</MontoTotalLinea>");
            builder.append("</LineaDetalle>");
        }
        builder.append("</DetalleServicio>");
        builder.append("</").append(rootName).append(">");
        return builder.toString();
    }

    @Override
    public String signXml(String xml, ElectronicDocument document) {
        String signedXml = xmlSignerUtil.sign(xml);
        document.setSignedXml(signedXml);
        documentRepository.save(document);
        return signedXml;
    }

    @Override
    public void validateAgainstXsd(String xml) {
        xsdValidatorUtil.validate(xml);
    }

    @Override
    @Transactional
    public void sendToHacienda(ElectronicDocument document, String signedXml) {
        try {
            String tokenResponse = haciendaClient.obtenerToken();
            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
            String token = tokenJson.get("access_token").asText();

            Map<String, Object> payload = new HashMap<>();
            payload.put("clave", document.getKey());
            payload.put("fecha", document.getIssuedAt().toString());
            Map<String, String> issuer = new HashMap<>();
            issuer.put("tipoIdentificacion", document.getIssuer().getIdentificationType());
            issuer.put("numeroIdentificacion", document.getIssuer().getIdentification());
            payload.put("emisor", issuer);
            Map<String, String> receiver = new HashMap<>();
            receiver.put("tipoIdentificacion", document.getReceiver().getIdentificationType());
            receiver.put("numeroIdentificacion", document.getReceiver().getIdentification());
            payload.put("receptor", receiver);
            payload.put("comprobanteXml", Base64Util.encode(signedXml));

            String response = haciendaClient.enviarComprobante(token, HaciendaClient.construirPayloadEnvio(payload));
            recordSubmission(document, response);
        } catch (Exception e) {
            log.error("Error sending document {}: {}", document.getKey(), e.getMessage());
            registerRetry(document.getKey(), e.getMessage());
            throw new IllegalStateException("Unable to send document to Hacienda", e);
        }
    }

    @Override
    @Transactional
    public void queryStatus(String key) {
        ElectronicDocument document = documentRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("No existe comprobante con clave " + key));
        try {
            String tokenResponse = haciendaClient.obtenerToken();
            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
            String token = tokenJson.get("access_token").asText();
            String response = haciendaClient.consultarEstado(token, key);
            JsonNode statusJson = objectMapper.readTree(response);
            String status = statusJson.path("ind-estado").asText("PENDIENTE");
            String detail = statusJson.path("respuesta-xml").asText();
            updateStatus(document, status, detail);
        } catch (Exception e) {
            log.error("Error consulting status {}: {}", key, e.getMessage());
            registerRetry(key, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void registerRetry(String key, String errorMessage) {
        ElectronicDocument document = documentRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("No existe comprobante con clave " + key));
        RetryLog logEntry = new RetryLog();
        logEntry.setDocument(document);
        logEntry.setAttemptedAt(LocalDateTime.now());
        logEntry.setResult("PENDIENTE");
        logEntry.setErrorMessage(errorMessage);
        retryLogRepository.save(logEntry);
        retryPublisher.publishRetry(key, errorMessage);
    }

    @Override
    @Transactional
    public ReceiverMessage processReceiverMessage(ReceiverMessageRequest request) {
        ReceiverMessage message = new ReceiverMessage();
        message.setReferenceKey(request.getReferenceKey());
        message.setReferenceConsecutive(request.getReferenceConsecutive());
        message.setMessage(request.getMessage());
        message.setMessageDetail(request.getMessageDetail());
        message.setTaxCondition(request.getTaxCondition() != null ? String.valueOf(request.getTaxCondition()) : null);
        message.setIssuedAt(LocalDateTime.now());
        message.setMessageXml(buildReceiverMessageXml(message));
        return receiverMessageRepository.save(message);
    }

    private String buildReceiverMessageXml(ReceiverMessage message) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<MensajeReceptor xmlns=\"https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4\">");
        builder.append("<Clave>").append(message.getReferenceKey()).append("</Clave>");
        builder.append("<NumeroConsecutivoReceptor>").append(message.getReferenceConsecutive()).append("</NumeroConsecutivoReceptor>");
        builder.append("<Mensaje>").append(message.getMessage()).append("</Mensaje>");
        builder.append("<DetalleMensaje>").append(message.getMessageDetail()).append("</DetalleMensaje>");
        builder.append("<CondicionImpuesto>").append(message.getTaxCondition()).append("</CondicionImpuesto>");
        builder.append("<FechaEmision>").append(message.getIssuedAt()).append("</FechaEmision>");
        builder.append("</MensajeReceptor>");
        return builder.toString();
    }

    private void recordSubmission(ElectronicDocument document, String response) {
        SubmissionHistory history = new SubmissionHistory();
        history.setDocument(document);
        history.setSentAt(LocalDateTime.now());
        history.setStatus("ENVIADO");
        history.setHaciendaResponse(response);
        submissionHistoryRepository.save(history);
        document.setHaciendaStatus("ENVIADO");
        documentRepository.save(document);
    }

    private void updateStatus(ElectronicDocument document, String status, String detail) {
        document.setHaciendaStatus(status);
        documentRepository.save(document);
        HaciendaStatus haciendaStatus = haciendaStatusRepository.findByDocumentKey(document.getKey())
                .orElseGet(HaciendaStatus::new);
        haciendaStatus.setDocument(document);
        haciendaStatus.setStatus(status);
        haciendaStatus.setDetail(detail);
        haciendaStatus.setUpdatedAt(LocalDateTime.now());
        haciendaStatusRepository.save(haciendaStatus);
    }

    private String generateConsecutive(ElectronicDocument.DocumentType type) {
        return switch (type) {
            case FACTURA -> "00100001010000000001";
            case NOTA_CREDITO -> "00100001020000000001";
            case NOTA_DEBITO -> "00100001030000000001";
            case TIQUETE -> "00100001040000000001";
            case MENSAJE_RECEPTOR -> "00100001050000000001";
            case FACTURA_EXPORTACION -> "00100001060000000001";
            case FACTURA_COMPRA -> "00100001070000000001";
            case RECIBO_ELECTRONICO_PAGO -> "00100001080000000001";
        };
    }
}
