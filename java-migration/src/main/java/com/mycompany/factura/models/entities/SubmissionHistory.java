package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "envio_historial")
public class SubmissionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprobante_id")
    private ElectronicDocument document;

    @JsonProperty("fechaEnvio")
    @Column(name = "fecha_envio")
    private LocalDateTime sentAt;

    @JsonProperty("estado")
    @Column(name = "estado")
    private String status;

    @Lob
    @JsonProperty("respuestaHacienda")
    @Column(name = "respuesta_hacienda", columnDefinition = "TEXT")
    private String haciendaResponse;

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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHaciendaResponse() {
        return haciendaResponse;
    }

    public void setHaciendaResponse(String haciendaResponse) {
        this.haciendaResponse = haciendaResponse;
    }
}
