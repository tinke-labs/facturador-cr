package com.mycompany.factura.config;

import com.mycompany.factura.models.dto.DocumentRequest;
import com.mycompany.factura.models.entities.ElectronicDocument;
import com.mycompany.factura.models.entities.Issuer;
import com.mycompany.factura.models.entities.LineItem;
import com.mycompany.factura.models.entities.Receiver;
import com.mycompany.factura.models.entities.Tax;
import com.mycompany.factura.models.entities.Totals;
import com.mycompany.factura.service.DocumentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("demo")
public class DemoFacturaRunner implements CommandLineRunner {

    private final DocumentService documentService;

    public DemoFacturaRunner(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public void run(String... args) {
        DocumentRequest request = new DocumentRequest();
        request.setType(ElectronicDocument.DocumentType.FACTURA);

        Issuer issuer = new Issuer();
        issuer.setName("Empresa Demo S.A.");
        issuer.setIdentification("3101123456");
        issuer.setIdentificationType("02");
        issuer.setEmail("demo@empresa.com");
        request.setIssuer(issuer);

        Receiver receiver = new Receiver();
        receiver.setName("Cliente Demo");
        receiver.setIdentification("114410111");
        receiver.setIdentificationType("01");
        receiver.setEmail("cliente@demo.com");
        request.setReceiver(receiver);

        Totals totals = new Totals();
        totals.setSaleTotal(new BigDecimal("100.00"));
        totals.setNetSaleTotal(new BigDecimal("100.00"));
        totals.setTaxTotal(new BigDecimal("13.00"));
        totals.setDocumentTotal(new BigDecimal("113.00"));
        request.setTotals(totals);

        LineItem lineItem = new LineItem();
        lineItem.setLineNumber(1);
        lineItem.setCode("001");
        lineItem.setDescription("Servicio profesional");
        lineItem.setQuantity(new BigDecimal("1"));
        lineItem.setUnitPrice(new BigDecimal("100.00"));
        lineItem.setSubtotal(new BigDecimal("100.00"));
        lineItem.setLineTotal(new BigDecimal("113.00"));
        Tax tax = new Tax();
        tax.setCode("01");
        tax.setRate(new BigDecimal("13"));
        tax.setAmount(new BigDecimal("13.00"));
        lineItem.setTax(tax);
        request.setLineItems(List.of(lineItem));

        ElectronicDocument document = documentService.createDocument(request);
        String xml = documentService.generateXml(document);
        documentService.validateAgainstXsd(xml);
        String signedXml = documentService.signXml(xml, document);
        documentService.sendToHacienda(document, signedXml);
    }
}
