package com.mycompany.factura.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "factura.firma")
public class FirmaProperties {

    private String certificadoPath;
    private String certificadoPin;

    public String getCertificadoPath() {
        return certificadoPath;
    }

    public void setCertificadoPath(String certificadoPath) {
        this.certificadoPath = certificadoPath;
    }

    public String getCertificadoPin() {
        return certificadoPin;
    }

    public void setCertificadoPin(String certificadoPin) {
        this.certificadoPin = certificadoPin;
    }
}
