package com.mycompany.factura.utils.xml;

import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class XmlSignerUtil {

    static {
        Init.init();
    }

    private final CertificateLoader certificateLoader;

    public XmlSignerUtil(CertificateLoader certificateLoader) {
        this.certificateLoader = certificateLoader;
    }

    public String sign(String unsignedXml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document document = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(unsignedXml.getBytes(StandardCharsets.UTF_8)));

            KeyStore.PrivateKeyEntry entry = certificateLoader.loadPrivateKey();
            PrivateKey privateKey = entry.getPrivateKey();
            X509Certificate certificate = (X509Certificate) entry.getCertificate();

            XMLSignature signature = new XMLSignature(document, "", XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);
            document.getDocumentElement().appendChild(signature.getElement());

            Transforms transforms = new Transforms(document);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
            signature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA256);

            signature.addKeyInfo(certificate);
            signature.addKeyInfo(certificate.getPublicKey());
            signature.sign(privateKey);

            return XmlTransformer.toString(document);
        } catch (Exception e) {
            throw new IllegalStateException("Error firmando XML: " + e.getMessage(), e);
        }
    }
}
