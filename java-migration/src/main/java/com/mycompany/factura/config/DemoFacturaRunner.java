package com.mycompany.factura.config;

import com.mycompany.factura.models.dto.ComprobanteRequest;
import com.mycompany.factura.models.entities.Comprobante;
import com.mycompany.factura.models.entities.DetalleLinea;
import com.mycompany.factura.models.entities.Emisor;
import com.mycompany.factura.models.entities.Impuesto;
import com.mycompany.factura.models.entities.Receptor;
import com.mycompany.factura.models.entities.Totales;
import com.mycompany.factura.service.ComprobanteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("demo")
public class DemoFacturaRunner implements CommandLineRunner {

    private final ComprobanteService comprobanteService;

    public DemoFacturaRunner(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    @Override
    public void run(String... args) {
        ComprobanteRequest request = new ComprobanteRequest();
        request.setTipo(Comprobante.TipoComprobante.FACTURA);

        Emisor emisor = new Emisor();
        emisor.setNombre("Empresa Demo S.A.");
        emisor.setIdentificacion("3101123456");
        emisor.setTipoIdentificacion("02");
        emisor.setEmail("demo@empresa.com");
        request.setEmisor(emisor);

        Receptor receptor = new Receptor();
        receptor.setNombre("Cliente Demo");
        receptor.setIdentificacion("114410111");
        receptor.setTipoIdentificacion("01");
        receptor.setEmail("cliente@demo.com");
        request.setReceptor(receptor);

        Totales totales = new Totales();
        totales.setTotalVenta(new BigDecimal("100.00"));
        totales.setTotalVentaNeta(new BigDecimal("100.00"));
        totales.setTotalImpuesto(new BigDecimal("13.00"));
        totales.setTotalComprobante(new BigDecimal("113.00"));
        request.setTotales(totales);

        DetalleLinea detalle = new DetalleLinea();
        detalle.setNumeroLinea(1);
        detalle.setCodigo("001");
        detalle.setDescripcion("Servicio profesional");
        detalle.setCantidad(new BigDecimal("1"));
        detalle.setPrecioUnitario(new BigDecimal("100.00"));
        detalle.setSubtotal(new BigDecimal("100.00"));
        detalle.setTotalLinea(new BigDecimal("113.00"));
        Impuesto impuesto = new Impuesto();
        impuesto.setCodigo("01");
        impuesto.setTarifa(new BigDecimal("13"));
        impuesto.setMonto(new BigDecimal("13.00"));
        detalle.setImpuesto(impuesto);
        request.setDetalles(List.of(detalle));

        Comprobante comprobante = comprobanteService.crearComprobante(request);
        String xml = comprobanteService.generarXml(comprobante);
        comprobanteService.validarContraXsd(xml);
        String xmlFirmado = comprobanteService.firmarXml(xml, comprobante);
        comprobanteService.enviarAHacienda(comprobante, xmlFirmado);
    }
}
