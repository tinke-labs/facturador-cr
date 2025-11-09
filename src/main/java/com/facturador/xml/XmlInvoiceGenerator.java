package com.facturador.xml;

import com.facturador.invoice.model.Invoice;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import org.springframework.stereotype.Component;

@Component
public class XmlInvoiceGenerator {

    public String generate(Invoice invoice) {
        try {
            InvoiceXmlModel model = InvoiceXmlModel.from(invoice);
            JAXBContext context = JAXBContext.newInstance(InvoiceXmlModel.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            marshaller.marshal(model, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to generate XML", e);
        }
    }
}
