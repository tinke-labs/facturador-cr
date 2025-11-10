package com.mycompany.factura.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_linea")
public class DetalleLinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroLinea;
    private String codigo;
    private String descripcion;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal totalLinea;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "codigo", column = @Column(name = "codigo_impuesto")),
            @AttributeOverride(name = "tarifa", column = @Column(name = "tarifa_impuesto")),
            @AttributeOverride(name = "monto", column = @Column(name = "monto_impuesto")),
            @AttributeOverride(name = "exonerado", column = @Column(name = "monto_exonerado"))
    })
    private Impuesto impuesto;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "documento", column = @Column(name = "exoneracion_documento")),
            @AttributeOverride(name = "fechaEmision", column = @Column(name = "exoneracion_fecha")),
            @AttributeOverride(name = "institucion", column = @Column(name = "exoneracion_institucion")),
            @AttributeOverride(name = "porcentajeCompra", column = @Column(name = "exoneracion_porcentaje"))
    })
    private Exoneracion exoneracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprobante_id")
    private Comprobante comprobante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(Integer numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotalLinea() {
        return totalLinea;
    }

    public void setTotalLinea(BigDecimal totalLinea) {
        this.totalLinea = totalLinea;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public Exoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(Exoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }
}
