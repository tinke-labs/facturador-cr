package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Totals {

    @JsonProperty("totalServGravados")
    private BigDecimal taxedServicesTotal;

    @JsonProperty("totalServExentos")
    private BigDecimal exemptServicesTotal;

    @JsonProperty("totalMercanciasGravadas")
    private BigDecimal taxedGoodsTotal;

    @JsonProperty("totalMercanciasExentas")
    private BigDecimal exemptGoodsTotal;

    @JsonProperty("totalGravado")
    private BigDecimal taxedTotal;

    @JsonProperty("totalExento")
    private BigDecimal exemptTotal;

    @JsonProperty("totalVenta")
    private BigDecimal saleTotal;

    @JsonProperty("totalDescuentos")
    private BigDecimal discountTotal;

    @JsonProperty("totalVentaNeta")
    private BigDecimal netSaleTotal;

    @JsonProperty("totalImpuesto")
    private BigDecimal taxTotal;

    @JsonProperty("totalComprobante")
    private BigDecimal documentTotal;

    public BigDecimal getTaxedServicesTotal() {
        return taxedServicesTotal;
    }

    public void setTaxedServicesTotal(BigDecimal taxedServicesTotal) {
        this.taxedServicesTotal = taxedServicesTotal;
    }

    public BigDecimal getExemptServicesTotal() {
        return exemptServicesTotal;
    }

    public void setExemptServicesTotal(BigDecimal exemptServicesTotal) {
        this.exemptServicesTotal = exemptServicesTotal;
    }

    public BigDecimal getTaxedGoodsTotal() {
        return taxedGoodsTotal;
    }

    public void setTaxedGoodsTotal(BigDecimal taxedGoodsTotal) {
        this.taxedGoodsTotal = taxedGoodsTotal;
    }

    public BigDecimal getExemptGoodsTotal() {
        return exemptGoodsTotal;
    }

    public void setExemptGoodsTotal(BigDecimal exemptGoodsTotal) {
        this.exemptGoodsTotal = exemptGoodsTotal;
    }

    public BigDecimal getTaxedTotal() {
        return taxedTotal;
    }

    public void setTaxedTotal(BigDecimal taxedTotal) {
        this.taxedTotal = taxedTotal;
    }

    public BigDecimal getExemptTotal() {
        return exemptTotal;
    }

    public void setExemptTotal(BigDecimal exemptTotal) {
        this.exemptTotal = exemptTotal;
    }

    public BigDecimal getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(BigDecimal saleTotal) {
        this.saleTotal = saleTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getNetSaleTotal() {
        return netSaleTotal;
    }

    public void setNetSaleTotal(BigDecimal netSaleTotal) {
        this.netSaleTotal = netSaleTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getDocumentTotal() {
        return documentTotal;
    }

    public void setDocumentTotal(BigDecimal documentTotal) {
        this.documentTotal = documentTotal;
    }
}
