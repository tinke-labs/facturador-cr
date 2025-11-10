package com.mycompany.factura.controller;

import com.mycompany.factura.models.dto.DocumentRequest;
import com.mycompany.factura.models.dto.ReceiverMessageRequest;
import com.mycompany.factura.models.entities.ElectronicDocument;
import com.mycompany.factura.models.entities.ReceiverMessage;
import com.mycompany.factura.repository.DocumentRepository;
import com.mycompany.factura.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comprobantes")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;

    public DocumentController(DocumentService documentService,
                              DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
    }

    @PostMapping("/factura")
    public ResponseEntity<Map<String, Object>> crearFactura(@Validated @RequestBody DocumentRequest request) {
        request.setType(ElectronicDocument.DocumentType.FACTURA);
        return ResponseEntity.ok(runPipeline(request));
    }

    @PostMapping("/nota-credito")
    public ResponseEntity<Map<String, Object>> crearNotaCredito(@Validated @RequestBody DocumentRequest request) {
        request.setType(ElectronicDocument.DocumentType.NOTA_CREDITO);
        return ResponseEntity.ok(runPipeline(request));
    }

    @PostMapping("/nota-debito")
    public ResponseEntity<Map<String, Object>> crearNotaDebito(@Validated @RequestBody DocumentRequest request) {
        request.setType(ElectronicDocument.DocumentType.NOTA_DEBITO);
        return ResponseEntity.ok(runPipeline(request));
    }

    @PostMapping("/tiquete")
    public ResponseEntity<Map<String, Object>> crearTiquete(@Validated @RequestBody DocumentRequest request) {
        request.setType(ElectronicDocument.DocumentType.TIQUETE);
        return ResponseEntity.ok(runPipeline(request));
    }

    @PostMapping("/mensaje-receptor")
    public ResponseEntity<ReceiverMessage> crearMensajeReceptor(@Validated @RequestBody ReceiverMessageRequest request) {
        ReceiverMessage mensaje = documentService.processReceiverMessage(request);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/{clave}/validar")
    public ResponseEntity<String> validar(@PathVariable String clave) {
        ElectronicDocument document = getDocument(clave);
        documentService.validateAgainstXsd(document.getSignedXml());
        return ResponseEntity.ok("XML válido contra XSD");
    }

    @PostMapping("/{clave}/firmar")
    public ResponseEntity<String> firmar(@PathVariable String clave) {
        ElectronicDocument document = getDocument(clave);
        String xmlGenerado = documentService.generateXml(document);
        String xmlFirmado = documentService.signXml(xmlGenerado, document);
        return ResponseEntity.ok(xmlFirmado);
    }

    @PostMapping("/{clave}/enviar")
    public ResponseEntity<String> enviar(@PathVariable String clave) {
        ElectronicDocument document = getDocument(clave);
        documentService.sendToHacienda(document, document.getSignedXml());
        return ResponseEntity.ok("Envío en proceso");
    }

    @GetMapping("/{clave}/estado")
    public ResponseEntity<String> consultarEstado(@PathVariable String clave) {
        documentService.queryStatus(clave);
        return ResponseEntity.ok("Consulta realizada");
    }

    @PostMapping("/{clave}/reintentar")
    public ResponseEntity<String> reintentar(@PathVariable String clave, @RequestParam(required = false) String motivo) {
        documentService.registerRetry(clave, motivo != null ? motivo : "Reintento manual");
        return ResponseEntity.ok("Reintento registrado");
    }

    private Map<String, Object> runPipeline(DocumentRequest request) {
        ElectronicDocument document = documentService.createDocument(request);
        String xml = documentService.generateXml(document);
        documentService.validateAgainstXsd(xml);
        String xmlFirmado = documentService.signXml(xml, document);
        documentService.sendToHacienda(document, xmlFirmado);
        return Map.of(
                "clave", document.getKey(),
                "consecutivo", document.getConsecutive(),
                "xmlFirmado", xmlFirmado
        );
    }

    private ElectronicDocument getDocument(String clave) {
        return documentRepository.findByKey(clave)
                .orElseThrow(() -> new IllegalArgumentException("No existe comprobante con clave " + clave));
    }
}
