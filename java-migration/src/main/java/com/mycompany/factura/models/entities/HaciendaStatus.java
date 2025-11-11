package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estado_hacienda")
public class HaciendaStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprobante_id")
    private ElectronicDocument document;

    @JsonProperty("estado")
    @Column(name = "estado")
    private String status;

    @JsonProperty("detalle")
    @Column(name = "detalle")
    private String detail;

    @JsonProperty("fechaActualizacion")
    @Column(name = "fecha_actualizacion")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ElectronicDocument getDocument() {
        return document;
    }

    public void setDocument(ElectronicDocument document) {
        this.document = document;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
