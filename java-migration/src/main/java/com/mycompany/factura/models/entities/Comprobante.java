package com.mycompany.factura.models.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clave;
    private String consecutivo;

    @Enumerated(EnumType.STRING)
    private TipoComprobante tipo;

    private LocalDateTime fechaEmision;

    @Embedded
    private Emisor emisor;

    @Embedded
    private Receptor receptor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "totalServGravados", column = @Column(name = "total_serv_gravados")),
            @AttributeOverride(name = "totalServExentos", column = @Column(name = "total_serv_exentos")),
            @AttributeOverride(name = "totalMercanciasGravadas", column = @Column(name = "total_mercancias_gravadas")),
            @AttributeOverride(name = "totalMercanciasExentas", column = @Column(name = "total_mercancias_exentas")),
            @AttributeOverride(name = "totalGravado", column = @Column(name = "total_gravado")),
            @AttributeOverride(name = "totalExento", column = @Column(name = "total_exento")),
            @AttributeOverride(name = "totalVenta", column = @Column(name = "total_venta")),
            @AttributeOverride(name = "totalDescuentos", column = @Column(name = "total_descuentos")),
            @AttributeOverride(name = "totalVentaNeta", column = @Column(name = "total_venta_neta")),
            @AttributeOverride(name = "totalImpuesto", column = @Column(name = "total_impuesto")),
            @AttributeOverride(name = "totalComprobante", column = @Column(name = "total_comprobante"))
    })
    private Totales totales;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String xmlFirmado;

    private String estadoHacienda;

    @OneToMany(mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleLinea> detalles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public TipoComprobante getTipo() {
        return tipo;
    }

    public void setTipo(TipoComprobante tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public Totales getTotales() {
        return totales;
    }

    public void setTotales(Totales totales) {
        this.totales = totales;
    }

    public String getXmlFirmado() {
        return xmlFirmado;
    }

    public void setXmlFirmado(String xmlFirmado) {
        this.xmlFirmado = xmlFirmado;
    }

    public String getEstadoHacienda() {
        return estadoHacienda;
    }

    public void setEstadoHacienda(String estadoHacienda) {
        this.estadoHacienda = estadoHacienda;
    }

    public List<DetalleLinea> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleLinea> detalles) {
        this.detalles = detalles;
    }

    public enum TipoComprobante {
        FACTURA, NOTA_CREDITO, NOTA_DEBITO, TIQUETE, MENSAJE_RECEPTOR,
        FACTURA_EXPORTACION, FACTURA_COMPRA, RECIBO_ELECTRONICO_PAGO
    }
}
