package com.mycompany.factura.service;

import com.mycompany.factura.models.dto.ComprobanteRequest;
import com.mycompany.factura.models.entities.Comprobante;
import com.mycompany.factura.models.entities.MensajeReceptor;
import com.mycompany.factura.models.dto.MensajeReceptorRequest;

public interface ComprobanteService {

    Comprobante crearComprobante(ComprobanteRequest request);

    String generarXml(Comprobante comprobante);

    String firmarXml(String xml, Comprobante comprobante);

    void validarContraXsd(String xml);

    void enviarAHacienda(Comprobante comprobante, String xmlFirmado);

    void consultarEstado(String clave);

    void registrarReintento(String clave, String mensajeError);

    MensajeReceptor procesarMensajeReceptor(MensajeReceptorRequest request);
}
