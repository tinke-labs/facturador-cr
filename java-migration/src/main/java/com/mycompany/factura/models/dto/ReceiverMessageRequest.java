package com.mycompany.factura.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReceiverMessageRequest {

    @NotBlank
    @JsonProperty("claveReferencia")
    private String referenceKey;

    @NotBlank
    @JsonProperty("consecutivoReferencia")
    private String referenceConsecutive;

    @NotBlank
    @JsonProperty("mensaje")
    private String message;

    @NotBlank
    @JsonProperty("detalleMensaje")
    private String messageDetail;

    @NotNull
    @JsonProperty("condicionImpuesto")
    private Integer taxCondition;

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

    public Integer getTaxCondition() {
        return taxCondition;
    }

    public void setTaxCondition(Integer taxCondition) {
        this.taxCondition = taxCondition;
    }
}
