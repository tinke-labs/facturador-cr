package com.mycompany.factura.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlJsonConverter {

    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public String xmlToJson(String xml) {
        try {
            JsonNode node = xmlMapper.readTree(xml.getBytes());
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error convirtiendo XML a JSON", e);
        }
    }

    public String jsonToXml(String json) {
        try {
            JsonNode node = jsonMapper.readTree(json);
            return xmlMapper.writer().withRootName("Comprobante").writeValueAsString(node);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error convirtiendo JSON a XML", e);
        }
    }
}
