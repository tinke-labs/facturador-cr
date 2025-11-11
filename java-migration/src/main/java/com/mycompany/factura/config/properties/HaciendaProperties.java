package com.mycompany.factura.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "factura.hacienda")
public class HaciendaProperties {

    private String tokenUrl;
    private String recepcionUrl;
    private String consultaUrl;
    private String clientId;
    private String clientSecret;

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getRecepcionUrl() {
        return recepcionUrl;
    }

    public void setRecepcionUrl(String recepcionUrl) {
        this.recepcionUrl = recepcionUrl;
    }

    public String getConsultaUrl() {
        return consultaUrl;
    }

    public void setConsultaUrl(String consultaUrl) {
        this.consultaUrl = consultaUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
