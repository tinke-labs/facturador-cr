package com.mycompany.factura.utils.xml;

import com.mycompany.factura.config.properties.FirmaProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.springframework.stereotype.Component;

@Component
public class CertificateLoader {

    private final FirmaProperties firmaProperties;

    public CertificateLoader(FirmaProperties firmaProperties) {
        this.firmaProperties = firmaProperties;
    }

    public KeyStore.PrivateKeyEntry loadPrivateKey() {
        try (FileInputStream inputStream = new FileInputStream(firmaProperties.getCertificadoPath())) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] password = firmaProperties.getCertificadoPin().toCharArray();
            keyStore.load(inputStream, password);
            String alias = keyStore.aliases().nextElement();
            Key key = keyStore.getKey(alias, password);
            if (!(key instanceof PrivateKey privateKey)) {
                throw new IllegalStateException("No se pudo obtener la llave privada del certificado");
            }
            Certificate certificate = keyStore.getCertificate(alias);
            return new KeyStore.PrivateKeyEntry(privateKey, new Certificate[]{certificate});
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
            throw new IllegalStateException("Error cargando certificado: " + e.getMessage(), e);
        }
    }
}
