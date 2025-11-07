package com.facturador.xml;

import com.facturador.tenant.Tenant;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class XmlSigner {

    public String sign(Tenant tenant, String unsignedXml) {
        if (tenant.getCertificateInline() == null) {
            throw new IllegalStateException("Tenant certificate not configured");
        }
        // Placeholder for XAdES-EPES implementation. In production, decode certificate and sign XML.
        Base64.getDecoder().decode(tenant.getCertificateInline());
        return unsignedXml;
    }
}
