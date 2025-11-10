package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Receiver {

    @Column(name = "receptor_nombre")
    @JsonProperty("nombre")
    private String name;

    @Column(name = "receptor_identificacion")
    @JsonProperty("identificacion")
    private String identification;

    @Column(name = "receptor_tipo_identificacion")
    @JsonProperty("tipoIdentificacion")
    private String identificationType;

    @Column(name = "receptor_email")
    @JsonProperty("email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
