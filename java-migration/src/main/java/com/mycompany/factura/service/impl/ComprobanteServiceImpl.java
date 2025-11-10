package com.mycompany.factura.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.factura.config.properties.FacturaProperties;
import com.mycompany.factura.models.dto.ComprobanteRequest;
import com.mycompany.factura.models.entities.*;
import com.mycompany.factura.models.dto.MensajeReceptorRequest;
import com.mycompany.factura.repository.*;
import com.mycompany.factura.service.ComprobanteService;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class ComprobanteServiceImpl implements ComprobanteService {

    private static final Logger log = LoggerFactory.getLogger(ComprobanteServiceImpl.class);

    private final ComprobanteRepository comprobanteRepository;
    private final EnvioHistorialRepository envioHistorialRepository;
    private final EstadoHaciendaRepository estadoHaciendaRepository;
    private final ReintentoLogRepository reintentoLogRepository;
    private final MensajeReceptorRepository mensajeReceptorRepository;
    private final XmlSignerUtil xmlSignerUtil;
    private final XsdValidatorUtil xsdValidatorUtil;
    private final HaciendaClient haciendaClient;
    private final RetryPublisher retryPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ComprobanteServiceImpl(ComprobanteRepository comprobanteRepository,
                                  EnvioHistorialRepository envioHistorialRepository,
                                  EstadoHaciendaRepository estadoHaciendaRepository,
                                  ReintentoLogRepository reintentoLogRepository,
                                  MensajeReceptorRepository mensajeReceptorRepository,
                                  FacturaProperties facturaProperties,
                                  HaciendaClient haciendaClient,
                                  RetryPublisher retryPublisher,
                                  CertificateLoader certificateLoader) {
        this.comprobanteRepository = comprobanteRepository;
        this.envioHistorialRepository = envioHistorialRepository;
        this.estadoHaciendaRepository = estadoHaciendaRepository;
        this.reintentoLogRepository = reintentoLogRepository;
        this.mensajeReceptorRepository = mensajeReceptorRepository;
        this.haciendaClient = haciendaClient;
        this.retryPublisher = retryPublisher;
        this.xmlSignerUtil = new XmlSignerUtil(certificateLoader);
        this.xsdValidatorUtil = new XsdValidatorUtil(new File(facturaProperties.getXsdPath()));
    }

    @Override
    @Transactional
    public Comprobante crearComprobante(ComprobanteRequest request) {
        Comprobante comprobante = new Comprobante();
        comprobante.setClave(UuidGenerator.generarClave());
        comprobante.setConsecutivo(generarConsecutivo(request.getTipo()));
        comprobante.setTipo(request.getTipo());
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setEmisor(request.getEmisor());
        comprobante.setReceptor(request.getReceptor());
        comprobante.setTotales(request.getTotales());
        request.getDetalles().forEach(detalle -> detalle.setComprobante(comprobante));
        comprobante.setDetalles(request.getDetalles());
        comprobante.setEstadoHacienda("PENDIENTE");
        return comprobanteRepository.save(comprobante);
    }

    @Override
    public String generarXml(Comprobante comprobante) {
        StringBuilder builder = new StringBuilder();
        String rootName = switch (comprobante.getTipo()) {
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
        builder.append("<Clave>").append(comprobante.getClave()).append("</Clave>");
        builder.append("<CodigoActividad>001</CodigoActividad>");
        builder.append("<Consecutivo>").append(comprobante.getConsecutivo()).append("</Consecutivo>");
        builder.append("<FechaEmision>").append(comprobante.getFechaEmision()).append("</FechaEmision>");
        builder.append("<Emisor>");
        builder.append("<Nombre>").append(comprobante.getEmisor().getNombre()).append("</Nombre>");
        builder.append("<Identificacion><Tipo>").append(comprobante.getEmisor().getTipoIdentificacion()).append("</Tipo><Numero>")
                .append(comprobante.getEmisor().getIdentificacion()).append("</Numero></Identificacion>");
        builder.append("<CorreoElectronico>").append(comprobante.getEmisor().getEmail()).append("</CorreoElectronico>");
        builder.append("</Emisor>");
        builder.append("<Receptor>");
        builder.append("<Nombre>").append(comprobante.getReceptor().getNombre()).append("</Nombre>");
        builder.append("<Identificacion><Tipo>").append(comprobante.getReceptor().getTipoIdentificacion()).append("</Tipo><Numero>")
                .append(comprobante.getReceptor().getIdentificacion()).append("</Numero></Identificacion>");
        builder.append("<CorreoElectronico>").append(comprobante.getReceptor().getEmail()).append("</CorreoElectronico>");
        builder.append("</Receptor>");
        builder.append("<ResumenFactura>");
        Totales totales = comprobante.getTotales();
        builder.append("<TotalVenta>").append(totales.getTotalVenta()).append("</TotalVenta>");
        builder.append("<TotalVentaNeta>").append(totales.getTotalVentaNeta()).append("</TotalVentaNeta>");
        builder.append("<TotalImpuesto>").append(totales.getTotalImpuesto()).append("</TotalImpuesto>");
        builder.append("<TotalComprobante>").append(totales.getTotalComprobante()).append("</TotalComprobante>");
        builder.append("</ResumenFactura>");
        builder.append("<DetalleServicio>");
        for (DetalleLinea detalle : comprobante.getDetalles()) {
            builder.append("<LineaDetalle>");
            builder.append("<NumeroLinea>").append(detalle.getNumeroLinea()).append("</NumeroLinea>");
            builder.append("<Codigo>").append(detalle.getCodigo()).append("</Codigo>");
            builder.append("<Cantidad>").append(detalle.getCantidad()).append("</Cantidad>");
            builder.append("<UnidadMedida>Sp</UnidadMedida>");
            builder.append("<Detalle>").append(detalle.getDescripcion()).append("</Detalle>");
            builder.append("<PrecioUnitario>").append(detalle.getPrecioUnitario()).append("</PrecioUnitario>");
            builder.append("<MontoTotal>").append(detalle.getSubtotal()).append("</MontoTotal>");
            if (detalle.getDescuento() != null) {
                builder.append("<Descuento>").append(detalle.getDescuento()).append("</Descuento>");
            }
            if (detalle.getImpuesto() != null) {
                builder.append("<Impuesto><Codigo>").append(detalle.getImpuesto().getCodigo())
                        .append("</Codigo><Tarifa>").append(detalle.getImpuesto().getTarifa())
                        .append("</Tarifa><Monto>").append(detalle.getImpuesto().getMonto())
                        .append("</Monto></Impuesto>");
            }
            builder.append("<MontoTotalLinea>").append(detalle.getTotalLinea()).append("</MontoTotalLinea>");
            builder.append("</LineaDetalle>");
        }
        builder.append("</DetalleServicio>");
        builder.append("</").append(rootName).append(">");
        return builder.toString();
    }
    @Override
    public String firmarXml(String xml, Comprobante comprobante) {
        String xmlFirmado = xmlSignerUtil.sign(xml);
        comprobante.setXmlFirmado(xmlFirmado);
        comprobanteRepository.save(comprobante);
        return xmlFirmado;
    }

    @Override
    public void validarContraXsd(String xml) {
        xsdValidatorUtil.validate(xml);
    }

    @Override
    @Transactional
    public void enviarAHacienda(Comprobante comprobante, String xmlFirmado) {
        try {
            String tokenResponse = haciendaClient.obtenerToken();
            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
            String token = tokenJson.get("access_token").asText();

            Map<String, Object> payload = new HashMap<>();
            payload.put("clave", comprobante.getClave());
            payload.put("fecha", comprobante.getFechaEmision().toString());
            Map<String, String> emisor = new HashMap<>();
            emisor.put("tipoIdentificacion", comprobante.getEmisor().getTipoIdentificacion());
            emisor.put("numeroIdentificacion", comprobante.getEmisor().getIdentificacion());
            payload.put("emisor", emisor);
            Map<String, String> receptor = new HashMap<>();
            receptor.put("tipoIdentificacion", comprobante.getReceptor().getTipoIdentificacion());
            receptor.put("numeroIdentificacion", comprobante.getReceptor().getIdentificacion());
            payload.put("receptor", receptor);
            payload.put("comprobanteXml", Base64Util.encode(xmlFirmado));

            String respuesta = haciendaClient.enviarComprobante(token, HaciendaClient.construirPayloadEnvio(payload));
            registrarEnvio(comprobante, respuesta);
        } catch (Exception e) {
            log.error("Error enviando comprobante {}: {}", comprobante.getClave(), e.getMessage());
            registrarReintento(comprobante.getClave(), e.getMessage());
            throw new IllegalStateException("No se pudo enviar a Hacienda", e);
        }
    }

    @Override
    @Transactional
    public void consultarEstado(String clave) {
        Comprobante comprobante = comprobanteRepository.findByClave(clave)
                .orElseThrow(() -> new IllegalArgumentException("No existe comprobante con clave " + clave));
        try {
            String tokenResponse = haciendaClient.obtenerToken();
            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
            String token = tokenJson.get("access_token").asText();
            String respuesta = haciendaClient.consultarEstado(token, clave);
            JsonNode estadoJson = objectMapper.readTree(respuesta);
            String estado = estadoJson.path("ind-estado").asText("PENDIENTE");
            String detalle = estadoJson.path("respuesta-xml").asText();
            actualizarEstado(comprobante, estado, detalle);
        } catch (Exception e) {
            log.error("Error consultando estado {}: {}", clave, e.getMessage());
            registrarReintento(clave, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void registrarReintento(String clave, String mensajeError) {
        Comprobante comprobante = comprobanteRepository.findByClave(clave)
                .orElseThrow(() -> new IllegalArgumentException("No existe comprobante con clave " + clave));
        ReintentoLog logEntry = new ReintentoLog();
        logEntry.setComprobante(comprobante);
        logEntry.setFechaIntento(LocalDateTime.now());
        logEntry.setResultado("PENDIENTE");
        logEntry.setMensajeError(mensajeError);
        reintentoLogRepository.save(logEntry);
        retryPublisher.publicarReintento(clave, mensajeError);
    }

    @Override
    @Transactional
    public MensajeReceptor procesarMensajeReceptor(MensajeReceptorRequest request) {
        MensajeReceptor mensaje = new MensajeReceptor();
        mensaje.setClaveReferencia(request.getClaveReferencia());
        mensaje.setConsecutivoReferencia(request.getConsecutivoReferencia());
        mensaje.setMensaje(request.getMensaje());
        mensaje.setDetalleMensaje(request.getDetalleMensaje());
        mensaje.setCondicionImpuesto(String.valueOf(request.getCondicionImpuesto()));
        mensaje.setFechaEmision(LocalDateTime.now());
        mensaje.setXmlMensaje(construirMensajeReceptorXml(mensaje));
        return mensajeReceptorRepository.save(mensaje);
    }

    private String construirMensajeReceptorXml(MensajeReceptor mensaje) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<MensajeReceptor xmlns=\"https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4\">");
        builder.append("<Clave>").append(mensaje.getClaveReferencia()).append("</Clave>");
        builder.append("<NumeroConsecutivoReceptor>").append(mensaje.getConsecutivoReferencia()).append("</NumeroConsecutivoReceptor>");
        builder.append("<Mensaje>").append(mensaje.getMensaje()).append("</Mensaje>");
        builder.append("<DetalleMensaje>").append(mensaje.getDetalleMensaje()).append("</DetalleMensaje>");
        builder.append("<CondicionImpuesto>").append(mensaje.getCondicionImpuesto()).append("</CondicionImpuesto>");
        builder.append("<FechaEmision>").append(mensaje.getFechaEmision()).append("</FechaEmision>");
        builder.append("</MensajeReceptor>");
        return builder.toString();
    }

    private void registrarEnvio(Comprobante comprobante, String respuesta) {
        EnvioHistorial historial = new EnvioHistorial();
        historial.setComprobante(comprobante);
        historial.setFechaEnvio(LocalDateTime.now());
        historial.setEstado("ENVIADO");
        historial.setRespuestaHacienda(respuesta);
        envioHistorialRepository.save(historial);
    }

    private void actualizarEstado(Comprobante comprobante, String estado, String detalle) {
        comprobante.setEstadoHacienda(estado);
        comprobanteRepository.save(comprobante);
        EstadoHacienda estadoHacienda = estadoHaciendaRepository.findByComprobanteClave(comprobante.getClave())
                .orElseGet(EstadoHacienda::new);
        estadoHacienda.setComprobante(comprobante);
        estadoHacienda.setEstado(estado);
        estadoHacienda.setDetalle(detalle);
        estadoHacienda.setFechaActualizacion(LocalDateTime.now());
        estadoHaciendaRepository.save(estadoHacienda);
    }

    private String generarConsecutivo(Comprobante.TipoComprobante tipo) {
        return switch (tipo) {
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
