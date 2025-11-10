package com.mycompany.factura.controller;

import com.mycompany.factura.models.dto.ComprobanteRequest;
import com.mycompany.factura.models.dto.MensajeReceptorRequest;
import com.mycompany.factura.models.entities.Comprobante;
import com.mycompany.factura.models.entities.MensajeReceptor;
import com.mycompany.factura.repository.ComprobanteRepository;
import com.mycompany.factura.service.ComprobanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;
    private final ComprobanteRepository comprobanteRepository;

    public ComprobanteController(ComprobanteService comprobanteService,
                                 ComprobanteRepository comprobanteRepository) {
        this.comprobanteService = comprobanteService;
        this.comprobanteRepository = comprobanteRepository;
    }

    @PostMapping("/factura")
    public ResponseEntity<Map<String, Object>> crearFactura(@Validated @RequestBody ComprobanteRequest request) {
        request.setTipo(Comprobante.TipoComprobante.FACTURA);
        return ResponseEntity.ok(ejecutarPipeline(request));
    }

    @PostMapping("/nota-credito")
    public ResponseEntity<Map<String, Object>> crearNotaCredito(@Validated @RequestBody ComprobanteRequest request) {
        request.setTipo(Comprobante.TipoComprobante.NOTA_CREDITO);
        return ResponseEntity.ok(ejecutarPipeline(request));
    }

    @PostMapping("/nota-debito")
    public ResponseEntity<Map<String, Object>> crearNotaDebito(@Validated @RequestBody ComprobanteRequest request) {
        request.setTipo(Comprobante.TipoComprobante.NOTA_DEBITO);
        return ResponseEntity.ok(ejecutarPipeline(request));
    }

    @PostMapping("/tiquete")
    public ResponseEntity<Map<String, Object>> crearTiquete(@Validated @RequestBody ComprobanteRequest request) {
        request.setTipo(Comprobante.TipoComprobante.TIQUETE);
        return ResponseEntity.ok(ejecutarPipeline(request));
    }

    @PostMapping("/mensaje-receptor")
    public ResponseEntity<MensajeReceptor> crearMensajeReceptor(@Validated @RequestBody MensajeReceptorRequest request) {
        MensajeReceptor mensaje = comprobanteService.procesarMensajeReceptor(request);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/{clave}/validar")
    public ResponseEntity<String> validar(@PathVariable String clave) {
        Comprobante comprobante = obtenerComprobante(clave);
        comprobanteService.validarContraXsd(comprobante.getXmlFirmado());
        return ResponseEntity.ok("XML válido contra XSD");
    }

    @PostMapping("/{clave}/firmar")
    public ResponseEntity<String> firmar(@PathVariable String clave) {
        Comprobante comprobante = obtenerComprobante(clave);
        String xmlGenerado = comprobanteService.generarXml(comprobante);
        String xmlFirmado = comprobanteService.firmarXml(xmlGenerado, comprobante);
        return ResponseEntity.ok(xmlFirmado);
    }

    @PostMapping("/{clave}/enviar")
    public ResponseEntity<String> enviar(@PathVariable String clave) {
        Comprobante comprobante = obtenerComprobante(clave);
        comprobanteService.enviarAHacienda(comprobante, comprobante.getXmlFirmado());
        return ResponseEntity.ok("Envío en proceso");
    }

    @GetMapping("/{clave}/estado")
    public ResponseEntity<String> consultarEstado(@PathVariable String clave) {
        comprobanteService.consultarEstado(clave);
        return ResponseEntity.ok("Consulta realizada");
    }

    @PostMapping("/{clave}/reintentar")
    public ResponseEntity<String> reintentar(@PathVariable String clave, @RequestParam(required = false) String motivo) {
        comprobanteService.registrarReintento(clave, motivo != null ? motivo : "Reintento manual");
        return ResponseEntity.ok("Reintento registrado");
    }

    private Map<String, Object> ejecutarPipeline(ComprobanteRequest request) {
        Comprobante comprobante = comprobanteService.crearComprobante(request);
        String xml = comprobanteService.generarXml(comprobante);
        comprobanteService.validarContraXsd(xml);
        String xmlFirmado = comprobanteService.firmarXml(xml, comprobante);
        comprobanteService.enviarAHacienda(comprobante, xmlFirmado);
        return Map.of(
                "clave", comprobante.getClave(),
                "consecutivo", comprobante.getConsecutivo(),
                "xmlFirmado", xmlFirmado
        );
    }

    private Comprobante obtenerComprobante(String clave) {
        return comprobanteRepository.findByClave(clave)
                .orElseThrow(() -> new IllegalArgumentException("No existe comprobante con clave " + clave));
    }
}
