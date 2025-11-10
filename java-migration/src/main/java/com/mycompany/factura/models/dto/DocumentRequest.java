package com.mycompany.factura.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.factura.models.entities.ElectronicDocument;
import com.mycompany.factura.models.entities.Issuer;
import com.mycompany.factura.models.entities.LineItem;
import com.mycompany.factura.models.entities.Receiver;
import com.mycompany.factura.models.entities.Totals;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DocumentRequest {

    @NotNull
    @JsonProperty("tipo")
    private ElectronicDocument.DocumentType type;

    @NotNull
    @JsonProperty("emisor")
    private Issuer issuer;

    @NotNull
    @JsonProperty("receptor")
    private Receiver receiver;

    @NotNull
    @JsonProperty("totales")
    private Totals totals;

    @JsonProperty("detalles")
    private List<LineItem> lineItems = new ArrayList<>();

    @JsonProperty("condicionImpuesto")
    private String taxCondition;

    public ElectronicDocument.DocumentType getType() {
        return type;
    }

    public void setType(ElectronicDocument.DocumentType type) {
        this.type = type;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Totals getTotals() {
        return totals;
    }

    public void setTotals(Totals totals) {
        this.totals = totals;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getTaxCondition() {
        return taxCondition;
    }

    public void setTaxCondition(String taxCondition) {
        this.taxCondition = taxCondition;
    }
}
