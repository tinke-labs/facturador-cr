package com.mycompany.factura.models.entities;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Totales {

    private BigDecimal totalServGravados;
    private BigDecimal totalServExentos;
    private BigDecimal totalMercanciasGravadas;
    private BigDecimal totalMercanciasExentas;
    private BigDecimal totalGravado;
    private BigDecimal totalExento;
    private BigDecimal totalVenta;
    private BigDecimal totalDescuentos;
    private BigDecimal totalVentaNeta;
    private BigDecimal totalImpuesto;
    private BigDecimal totalComprobante;

    public BigDecimal getTotalServGravados() {
        return totalServGravados;
    }

    public void setTotalServGravados(BigDecimal totalServGravados) {
        this.totalServGravados = totalServGravados;
    }

    public BigDecimal getTotalServExentos() {
        return totalServExentos;
    }

    public void setTotalServExentos(BigDecimal totalServExentos) {
        this.totalServExentos = totalServExentos;
    }

    public BigDecimal getTotalMercanciasGravadas() {
        return totalMercanciasGravadas;
    }

    public void setTotalMercanciasGravadas(BigDecimal totalMercanciasGravadas) {
        this.totalMercanciasGravadas = totalMercanciasGravadas;
    }

    public BigDecimal getTotalMercanciasExentas() {
        return totalMercanciasExentas;
    }

    public void setTotalMercanciasExentas(BigDecimal totalMercanciasExentas) {
        this.totalMercanciasExentas = totalMercanciasExentas;
    }

    public BigDecimal getTotalGravado() {
        return totalGravado;
    }

    public void setTotalGravado(BigDecimal totalGravado) {
        this.totalGravado = totalGravado;
    }

    public BigDecimal getTotalExento() {
        return totalExento;
    }

    public void setTotalExento(BigDecimal totalExento) {
        this.totalExento = totalExento;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotalVentaNeta() {
        return totalVentaNeta;
    }

    public void setTotalVentaNeta(BigDecimal totalVentaNeta) {
        this.totalVentaNeta = totalVentaNeta;
    }

    public BigDecimal getTotalImpuesto() {
        return totalImpuesto;
    }

    public void setTotalImpuesto(BigDecimal totalImpuesto) {
        this.totalImpuesto = totalImpuesto;
    }

    public BigDecimal getTotalComprobante() {
        return totalComprobante;
    }

    public void setTotalComprobante(BigDecimal totalComprobante) {
        this.totalComprobante = totalComprobante;
    }
}
