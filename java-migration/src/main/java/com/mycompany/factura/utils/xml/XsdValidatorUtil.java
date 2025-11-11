package com.mycompany.factura.utils.xml;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.SAXException;

public class XsdValidatorUtil {

    private final File xsdFile;

    public XsdValidatorUtil(File xsdFile) {
        this.xsdFile = xsdFile;
    }

    public void validate(String xmlContent) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlContent)));
        } catch (SAXException | IOException e) {
            throw new IllegalArgumentException("XML no cumple con XSD: " + e.getMessage(), e);
        }
    }
}
