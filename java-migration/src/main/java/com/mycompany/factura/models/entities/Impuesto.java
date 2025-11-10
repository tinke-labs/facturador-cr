package com.mycompany.factura.models.entities;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Impuesto {

    private String codigo;
    private BigDecimal tarifa;
    private BigDecimal monto;
    private BigDecimal exonerado;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getExonerado() {
        return exonerado;
    }

    public void setExonerado(BigDecimal exonerado) {
        this.exonerado = exonerado;
    }
}
