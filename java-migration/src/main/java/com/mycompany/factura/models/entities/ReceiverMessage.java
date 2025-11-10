package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje_receptor")
public class ReceiverMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("claveReferencia")
    @Column(name = "clave_referencia")
    private String referenceKey;

    @JsonProperty("consecutivoReferencia")
    @Column(name = "consecutivo_referencia")
    private String referenceConsecutive;

    @JsonProperty("mensaje")
    @Column(name = "mensaje")
    private String message;

    @JsonProperty("detalleMensaje")
    @Column(name = "detalle_mensaje")
    private String messageDetail;

    @JsonProperty("condicionImpuesto")
    @Column(name = "condicion_impuesto")
    private String taxCondition;

    @JsonProperty("fechaEmision")
    @Column(name = "fecha_emision")
    private LocalDateTime issuedAt;

    @Lob
    @JsonProperty("xmlMensaje")
    @Column(name = "xml_mensaje", columnDefinition = "TEXT")
    private String messageXml;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public String getReferenceConsecutive() {
        return referenceConsecutive;
    }

    public void setReferenceConsecutive(String referenceConsecutive) {
        this.referenceConsecutive = referenceConsecutive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    public String getTaxCondition() {
        return taxCondition;
    }

    public void setTaxCondition(String taxCondition) {
        this.taxCondition = taxCondition;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getMessageXml() {
        return messageXml;
    }

    public void setMessageXml(String messageXml) {
        this.messageXml = messageXml;
    }
}
