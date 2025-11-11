package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class Exoneration {

    @JsonProperty("documento")
    private String document;

    @JsonProperty("fechaEmision")
    private LocalDate issuedAt;

    @JsonProperty("institucion")
    private String institution;

    @JsonProperty("porcentajeCompra")
    private String purchasePercentage;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDate issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPurchasePercentage() {
        return purchasePercentage;
    }

    public void setPurchasePercentage(String purchasePercentage) {
        this.purchasePercentage = purchasePercentage;
    }
}
