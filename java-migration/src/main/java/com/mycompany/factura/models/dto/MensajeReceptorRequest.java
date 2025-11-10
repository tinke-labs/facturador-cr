package com.mycompany.factura.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MensajeReceptorRequest {

    @NotBlank
    private String claveReferencia;

    @NotBlank
    private String consecutivoReferencia;

    @NotBlank
    private String mensaje;

    @NotBlank
    private String detalleMensaje;

    @NotNull
    private Integer condicionImpuesto;

    public String getClaveReferencia() {
        return claveReferencia;
    }

    public void setClaveReferencia(String claveReferencia) {
        this.claveReferencia = claveReferencia;
    }

    public String getConsecutivoReferencia() {
        return consecutivoReferencia;
    }

    public void setConsecutivoReferencia(String consecutivoReferencia) {
        this.consecutivoReferencia = consecutivoReferencia;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDetalleMensaje() {
        return detalleMensaje;
    }

    public void setDetalleMensaje(String detalleMensaje) {
        this.detalleMensaje = detalleMensaje;
    }

    public Integer getCondicionImpuesto() {
        return condicionImpuesto;
    }

    public void setCondicionImpuesto(Integer condicionImpuesto) {
        this.condicionImpuesto = condicionImpuesto;
    }
}
