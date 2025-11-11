package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_linea")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("numeroLinea")
    @Column(name = "numero_linea")
    private Integer lineNumber;

    @JsonProperty("codigo")
    @Column(name = "codigo")
    private String code;

    @JsonProperty("descripcion")
    @Column(name = "descripcion")
    private String description;

    @JsonProperty("cantidad")
    @Column(name = "cantidad")
    private BigDecimal quantity;

    @JsonProperty("precioUnitario")
    @Column(name = "precio_unitario")
    private BigDecimal unitPrice;

    @JsonProperty("subtotal")
    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @JsonProperty("descuento")
    @Column(name = "descuento")
    private BigDecimal discount;

    @JsonProperty("totalLinea")
    @Column(name = "total_linea")
    private BigDecimal lineTotal;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "codigo_impuesto")),
            @AttributeOverride(name = "rate", column = @Column(name = "tarifa_impuesto")),
            @AttributeOverride(name = "amount", column = @Column(name = "monto_impuesto")),
            @AttributeOverride(name = "exoneratedAmount", column = @Column(name = "monto_exonerado"))
    })
    @JsonProperty("impuesto")
    private Tax tax;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "document", column = @Column(name = "exoneracion_documento")),
            @AttributeOverride(name = "issuedAt", column = @Column(name = "exoneracion_fecha")),
            @AttributeOverride(name = "institution", column = @Column(name = "exoneracion_institucion")),
            @AttributeOverride(name = "purchasePercentage", column = @Column(name = "exoneracion_porcentaje"))
    })
    @JsonProperty("exoneracion")
    private Exoneration exoneration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprobante_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ElectronicDocument document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Exoneration getExoneration() {
        return exoneration;
    }

    public void setExoneration(Exoneration exoneration) {
        this.exoneration = exoneration;
    }

    public ElectronicDocument getDocument() {
        return document;
    }

    public void setDocument(ElectronicDocument document) {
        this.document = document;
    }
}
