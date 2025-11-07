package com.facturador.tenant;

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
