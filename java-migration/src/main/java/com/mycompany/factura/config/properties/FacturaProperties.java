package com.mycompany.factura.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "factura")
public class FacturaProperties {

    private String xsdPath;

    public String getXsdPath() {
        return xsdPath;
    }

    public void setXsdPath(String xsdPath) {
        this.xsdPath = xsdPath;
    }
}
