package com.mycompany.factura.models.entities;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class Exoneracion {

    private String documento;
    private LocalDate fechaEmision;
    private String institucion;
    private String porcentajeCompra;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getPorcentajeCompra() {
        return porcentajeCompra;
    }

    public void setPorcentajeCompra(String porcentajeCompra) {
        this.porcentajeCompra = porcentajeCompra;
    }
}
