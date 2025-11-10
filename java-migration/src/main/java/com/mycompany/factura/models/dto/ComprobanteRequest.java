package com.mycompany.factura.models.dto;

import com.mycompany.factura.models.entities.Comprobante;
import com.mycompany.factura.models.entities.DetalleLinea;
import com.mycompany.factura.models.entities.Emisor;
import com.mycompany.factura.models.entities.Receptor;
import com.mycompany.factura.models.entities.Totales;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComprobanteRequest {

    @NotNull
    private Comprobante.TipoComprobante tipo;

    @NotNull
    private Emisor emisor;

    @NotNull
    private Receptor receptor;

    @NotNull
    private Totales totales;

    private List<DetalleLinea> detalles = new ArrayList<>();

    private String condicionImpuesto;

    public Comprobante.TipoComprobante getTipo() {
        return tipo;
    }

    public void setTipo(Comprobante.TipoComprobante tipo) {
        this.tipo = tipo;
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

    public List<DetalleLinea> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleLinea> detalles) {
        this.detalles = detalles;
    }

    public String getCondicionImpuesto() {
        return condicionImpuesto;
    }

    public void setCondicionImpuesto(String condicionImpuesto) {
        this.condicionImpuesto = condicionImpuesto;
    }
}
