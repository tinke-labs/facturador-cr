package com.mycompany.factura.models.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje_receptor")
public class MensajeReceptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claveReferencia;
    private String consecutivoReferencia;
    private String mensaje;
    private String detalleMensaje;
    private String condicionImpuesto;
    private LocalDateTime fechaEmision;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String xmlMensaje;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCondicionImpuesto() {
        return condicionImpuesto;
    }

    public void setCondicionImpuesto(String condicionImpuesto) {
        this.condicionImpuesto = condicionImpuesto;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getXmlMensaje() {
        return xmlMensaje;
    }

    public void setXmlMensaje(String xmlMensaje) {
        this.xmlMensaje = xmlMensaje;
    }
}
