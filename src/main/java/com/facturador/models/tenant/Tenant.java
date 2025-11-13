package com.facturador.models.tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tenants")
public class Tenant {

    public enum Environment {
        SANDBOX,
        PRODUCTION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "api_key", nullable = false, unique = true)
    private String apiKey;

    @Column(name = "identification_type", nullable = false, length = 2)
    private String identificationType;

    @Column(name = "identification_number", nullable = false, length = 20)
    private String identificationNumber;

    @Column(name = "commercial_name")
    private String commercialName;

    @Column(name = "email")
    private String email;

    @Column(name = "branch_code", nullable = false, length = 3)
    private String branchCode;

    @Column(name = "terminal_code", nullable = false, length = 5)
    private String terminalCode;

    @Column(name = "situation", nullable = false, length = 1)
    private String situation = "1";

    @Column(name = "phone_country_code", length = 4)
    private String phoneCountryCode;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "province", length = 1)
    private String province;

    @Column(name = "canton", length = 2)
    private String canton;

    @Column(name = "district", length = 2)
    private String district;

    @Column(name = "neighborhood", length = 2)
    private String neighborhood;

    @Column(name = "other_signs")
    private String otherSigns;

    @Column(name = "certificate_inline", columnDefinition = "TEXT")
    private String certificateInline;

    @Column(name = "certificate_pin_encrypted")
    private String certificatePinEncrypted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment = Environment.SANDBOX;

    @Column(name = "next_invoice_sequence", nullable = false)
    private Long nextInvoiceSequence = 1L;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getOtherSigns() {
        return otherSigns;
    }

    public void setOtherSigns(String otherSigns) {
        this.otherSigns = otherSigns;
    }

    public String getCertificateInline() {
        return certificateInline;
    }

    public void setCertificateInline(String certificateInline) {
        this.certificateInline = certificateInline;
    }

    public String getCertificatePinEncrypted() {
        return certificatePinEncrypted;
    }

    public void setCertificatePinEncrypted(String certificatePinEncrypted) {
        this.certificatePinEncrypted = certificatePinEncrypted;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Long getNextInvoiceSequence() {
        return nextInvoiceSequence;
    }

    public void setNextInvoiceSequence(Long nextInvoiceSequence) {
        this.nextInvoiceSequence = nextInvoiceSequence;
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
