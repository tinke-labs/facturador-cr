package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comprobante")
public class ElectronicDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("clave")
    @Column(name = "clave")
    private String key;

    @JsonProperty("consecutivo")
    @Column(name = "consecutivo")
    private String consecutive;

    @Enumerated(EnumType.STRING)
    @JsonProperty("tipo")
    @Column(name = "tipo")
    private DocumentType type;

    @JsonProperty("fechaEmision")
    @Column(name = "fecha_emision")
    private LocalDateTime issuedAt;

    @Embedded
    @JsonProperty("emisor")
    private Issuer issuer;

    @Embedded
    @JsonProperty("receptor")
    private Receiver receiver;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "taxedServicesTotal", column = @Column(name = "total_serv_gravados")),
            @AttributeOverride(name = "exemptServicesTotal", column = @Column(name = "total_serv_exentos")),
            @AttributeOverride(name = "taxedGoodsTotal", column = @Column(name = "total_mercancias_gravadas")),
            @AttributeOverride(name = "exemptGoodsTotal", column = @Column(name = "total_mercancias_exentas")),
            @AttributeOverride(name = "taxedTotal", column = @Column(name = "total_gravado")),
            @AttributeOverride(name = "exemptTotal", column = @Column(name = "total_exento")),
            @AttributeOverride(name = "saleTotal", column = @Column(name = "total_venta")),
            @AttributeOverride(name = "discountTotal", column = @Column(name = "total_descuentos")),
            @AttributeOverride(name = "netSaleTotal", column = @Column(name = "total_venta_neta")),
            @AttributeOverride(name = "taxTotal", column = @Column(name = "total_impuesto")),
            @AttributeOverride(name = "documentTotal", column = @Column(name = "total_comprobante"))
    })
    @JsonProperty("totales")
    private Totals totals;

    @Lob
    @JsonProperty("xmlFirmado")
    @Column(name = "xml_firmado", columnDefinition = "TEXT")
    private String signedXml;

    @JsonProperty("estadoHacienda")
    @Column(name = "estado_hacienda")
    private String haciendaStatus;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("detalles")
    private List<LineItem> lineItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
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

    public String getSignedXml() {
        return signedXml;
    }

    public void setSignedXml(String signedXml) {
        this.signedXml = signedXml;
    }

    public String getHaciendaStatus() {
        return haciendaStatus;
    }

    public void setHaciendaStatus(String haciendaStatus) {
        this.haciendaStatus = haciendaStatus;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public enum DocumentType {
        FACTURA, NOTA_CREDITO, NOTA_DEBITO, TIQUETE, MENSAJE_RECEPTOR,
        FACTURA_EXPORTACION, FACTURA_COMPRA, RECIBO_ELECTRONICO_PAGO
    }
}
