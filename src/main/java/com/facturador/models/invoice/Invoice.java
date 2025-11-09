package com.facturador.models.invoice;

import com.facturador.models.tenant.Tenant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    public enum Status {
        CREATED,
        SENT,
        ACCEPTED,
        REJECTED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false, unique = true)
    private String clave;

    @Column(name = "activity_code", nullable = false, length = 6)
    private String activityCode;

    @Column(nullable = false)
    private String consecutive;

    @Column(name = "issue_date", nullable = false)
    private OffsetDateTime issueDate;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "sale_condition", nullable = false)
    private String saleCondition;

    @Column(name = "credit_term")
    private String creditTerm;

    @Column(nullable = false)
    private String currency;

    @Column(name = "exchange_rate", precision = 18, scale = 5)
    private BigDecimal exchangeRate;

    @Column(name = "payment_methods", columnDefinition = "TEXT")
    @Convert(converter = PaymentMethodListConverter.class)
    private List<String> paymentMethods = new ArrayList<>();

    @Column(name = "total", precision = 18, scale = 5, nullable = false)
    private BigDecimal total;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "receiver_identification_type", length = 2)
    private String receiverIdentificationType;

    @Column(name = "receiver_identification_number", length = 20)
    private String receiverIdentificationNumber;

    @Column(name = "receiver_commercial_name")
    private String receiverCommercialName;

    @Column(name = "receiver_province", length = 1)
    private String receiverProvince;

    @Column(name = "receiver_canton", length = 2)
    private String receiverCanton;

    @Column(name = "receiver_district", length = 2)
    private String receiverDistrict;

    @Column(name = "receiver_neighborhood", length = 2)
    private String receiverNeighborhood;

    @Column(name = "receiver_other_signs")
    private String receiverOtherSigns;

    @Column(name = "receiver_phone_country", length = 4)
    private String receiverPhoneCountry;

    @Column(name = "receiver_phone_number", length = 20)
    private String receiverPhoneNumber;

    @Column(name = "receiver_email")
    private String receiverEmail;

    @Column(name = "summary_total_taxed_services", precision = 18, scale = 5)
    private BigDecimal summaryTotalTaxedServices;

    @Column(name = "summary_total_exempt_services", precision = 18, scale = 5)
    private BigDecimal summaryTotalExemptServices;

    @Column(name = "summary_total_taxed_goods", precision = 18, scale = 5)
    private BigDecimal summaryTotalTaxedGoods;

    @Column(name = "summary_total_exempt_goods", precision = 18, scale = 5)
    private BigDecimal summaryTotalExemptGoods;

    @Column(name = "summary_total_taxed", precision = 18, scale = 5)
    private BigDecimal summaryTotalTaxed;

    @Column(name = "summary_total_exempt", precision = 18, scale = 5)
    private BigDecimal summaryTotalExempt;

    @Column(name = "summary_total_sale", precision = 18, scale = 5)
    private BigDecimal summaryTotalSale;

    @Column(name = "summary_total_discounts", precision = 18, scale = 5)
    private BigDecimal summaryTotalDiscounts;

    @Column(name = "summary_total_net_sale", precision = 18, scale = 5)
    private BigDecimal summaryTotalNetSale;

    @Column(name = "summary_total_tax", precision = 18, scale = 5)
    private BigDecimal summaryTotalTax;

    @Column(name = "summary_total_iva_refund", precision = 18, scale = 5)
    private BigDecimal summaryTotalIvaRefund;

    @Column(name = "summary_total_other_charges", precision = 18, scale = 5)
    private BigDecimal summaryTotalOtherCharges;

    @Column(name = "summary_total_voucher", precision = 18, scale = 5)
    private BigDecimal summaryTotalVoucher;

    @Column(name = "xml_path")
    private String xmlPath;

    @Column(name = "status", nullable = false)
    private String status = Status.CREATED.name();

    @Column(name = "location_header")
    private String locationHeader;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public OffsetDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(OffsetDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSaleCondition() {
        return saleCondition;
    }

    public void setSaleCondition(String saleCondition) {
        this.saleCondition = saleCondition;
    }

    public String getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(String creditTerm) {
        this.creditTerm = creditTerm;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods.clear();
        if (paymentMethods != null) {
            this.paymentMethods.addAll(paymentMethods);
        }
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReceiverIdentificationType() {
        return receiverIdentificationType;
    }

    public void setReceiverIdentificationType(String receiverIdentificationType) {
        this.receiverIdentificationType = receiverIdentificationType;
    }

    public String getReceiverIdentificationNumber() {
        return receiverIdentificationNumber;
    }

    public void setReceiverIdentificationNumber(String receiverIdentificationNumber) {
        this.receiverIdentificationNumber = receiverIdentificationNumber;
    }

    public String getReceiverCommercialName() {
        return receiverCommercialName;
    }

    public void setReceiverCommercialName(String receiverCommercialName) {
        this.receiverCommercialName = receiverCommercialName;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCanton() {
        return receiverCanton;
    }

    public void setReceiverCanton(String receiverCanton) {
        this.receiverCanton = receiverCanton;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverNeighborhood() {
        return receiverNeighborhood;
    }

    public void setReceiverNeighborhood(String receiverNeighborhood) {
        this.receiverNeighborhood = receiverNeighborhood;
    }

    public String getReceiverOtherSigns() {
        return receiverOtherSigns;
    }

    public void setReceiverOtherSigns(String receiverOtherSigns) {
        this.receiverOtherSigns = receiverOtherSigns;
    }

    public String getReceiverPhoneCountry() {
        return receiverPhoneCountry;
    }

    public void setReceiverPhoneCountry(String receiverPhoneCountry) {
        this.receiverPhoneCountry = receiverPhoneCountry;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public BigDecimal getSummaryTotalTaxedServices() {
        return summaryTotalTaxedServices;
    }

    public void setSummaryTotalTaxedServices(BigDecimal summaryTotalTaxedServices) {
        this.summaryTotalTaxedServices = summaryTotalTaxedServices;
    }

    public BigDecimal getSummaryTotalExemptServices() {
        return summaryTotalExemptServices;
    }

    public void setSummaryTotalExemptServices(BigDecimal summaryTotalExemptServices) {
        this.summaryTotalExemptServices = summaryTotalExemptServices;
    }

    public BigDecimal getSummaryTotalTaxedGoods() {
        return summaryTotalTaxedGoods;
    }

    public void setSummaryTotalTaxedGoods(BigDecimal summaryTotalTaxedGoods) {
        this.summaryTotalTaxedGoods = summaryTotalTaxedGoods;
    }

    public BigDecimal getSummaryTotalExemptGoods() {
        return summaryTotalExemptGoods;
    }

    public void setSummaryTotalExemptGoods(BigDecimal summaryTotalExemptGoods) {
        this.summaryTotalExemptGoods = summaryTotalExemptGoods;
    }

    public BigDecimal getSummaryTotalTaxed() {
        return summaryTotalTaxed;
    }

    public void setSummaryTotalTaxed(BigDecimal summaryTotalTaxed) {
        this.summaryTotalTaxed = summaryTotalTaxed;
    }

    public BigDecimal getSummaryTotalExempt() {
        return summaryTotalExempt;
    }

    public void setSummaryTotalExempt(BigDecimal summaryTotalExempt) {
        this.summaryTotalExempt = summaryTotalExempt;
    }

    public BigDecimal getSummaryTotalSale() {
        return summaryTotalSale;
    }

    public void setSummaryTotalSale(BigDecimal summaryTotalSale) {
        this.summaryTotalSale = summaryTotalSale;
    }

    public BigDecimal getSummaryTotalDiscounts() {
        return summaryTotalDiscounts;
    }

    public void setSummaryTotalDiscounts(BigDecimal summaryTotalDiscounts) {
        this.summaryTotalDiscounts = summaryTotalDiscounts;
    }

    public BigDecimal getSummaryTotalNetSale() {
        return summaryTotalNetSale;
    }

    public void setSummaryTotalNetSale(BigDecimal summaryTotalNetSale) {
        this.summaryTotalNetSale = summaryTotalNetSale;
    }

    public BigDecimal getSummaryTotalTax() {
        return summaryTotalTax;
    }

    public void setSummaryTotalTax(BigDecimal summaryTotalTax) {
        this.summaryTotalTax = summaryTotalTax;
    }

    public BigDecimal getSummaryTotalIvaRefund() {
        return summaryTotalIvaRefund;
    }

    public void setSummaryTotalIvaRefund(BigDecimal summaryTotalIvaRefund) {
        this.summaryTotalIvaRefund = summaryTotalIvaRefund;
    }

    public BigDecimal getSummaryTotalOtherCharges() {
        return summaryTotalOtherCharges;
    }

    public void setSummaryTotalOtherCharges(BigDecimal summaryTotalOtherCharges) {
        this.summaryTotalOtherCharges = summaryTotalOtherCharges;
    }

    public BigDecimal getSummaryTotalVoucher() {
        return summaryTotalVoucher;
    }

    public void setSummaryTotalVoucher(BigDecimal summaryTotalVoucher) {
        this.summaryTotalVoucher = summaryTotalVoucher;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationHeader() {
        return locationHeader;
    }

    public void setLocationHeader(String locationHeader) {
        this.locationHeader = locationHeader;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items.clear();
        if (items != null) {
            for (InvoiceItem item : items) {
                item.setInvoice(this);
                this.items.add(item);
            }
        }
    }

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
