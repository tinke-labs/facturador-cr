package com.facturador.models.invoice;

import com.facturador.models.tenant.Tenant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @Column(name = "commercial_code", nullable = false)
    private String commercialCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 18, scale = 5)
    private BigDecimal quantity;

    @Column(name = "measurement_unit", nullable = false)
    private String measurementUnit;

    @Column(name = "commercial_unit")
    private String commercialUnit;

    @Column(name = "unit_price", nullable = false, precision = 18, scale = 5)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 5)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 18, scale = 5)
    private BigDecimal discountAmount;

    @Column(name = "discount_reason")
    private String discountReason;

    @Column(name = "subtotal", nullable = false, precision = 18, scale = 5)
    private BigDecimal subtotal;

    @Column(name = "taxable_base", precision = 18, scale = 5)
    private BigDecimal taxableBase;

    @Column(name = "total_line_amount", nullable = false, precision = 18, scale = 5)
    private BigDecimal totalLineAmount;

    @OneToMany(mappedBy = "invoiceItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceTax> taxes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getCommercialCode() {
        return commercialCode;
    }

    public void setCommercialCode(String commercialCode) {
        this.commercialCode = commercialCode;
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

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public String getCommercialUnit() {
        return commercialUnit;
    }

    public void setCommercialUnit(String commercialUnit) {
        this.commercialUnit = commercialUnit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxableBase() {
        return taxableBase;
    }

    public void setTaxableBase(BigDecimal taxableBase) {
        this.taxableBase = taxableBase;
    }

    public BigDecimal getTotalLineAmount() {
        return totalLineAmount;
    }

    public void setTotalLineAmount(BigDecimal totalLineAmount) {
        this.totalLineAmount = totalLineAmount;
    }

    public List<InvoiceTax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<InvoiceTax> taxes) {
        this.taxes.clear();
        if (taxes != null) {
            for (InvoiceTax tax : taxes) {
                tax.setInvoiceItem(this);
                this.taxes.add(tax);
            }
        }
    }
}
