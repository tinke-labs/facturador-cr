package com.facturador.xml;

import com.facturador.invoice.model.Invoice;
import com.facturador.invoice.model.InvoiceItem;
import com.facturador.invoice.model.InvoiceTax;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "FacturaElectronica")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceXmlModel {

    private String clave;
    private String consecutive;
    private String currency;
    private double total;
    private String customerName;

    @XmlElementWrapper(name = "Lineas")
    @XmlElement(name = "Linea")
    private List<InvoiceXmlLine> lines;

    @XmlElementWrapper(name = "Impuestos")
    @XmlElement(name = "Impuesto")
    private List<InvoiceXmlTax> taxes;

    public static InvoiceXmlModel from(Invoice invoice) {
        InvoiceXmlModel model = new InvoiceXmlModel();
        model.clave = invoice.getClave();
        model.consecutive = invoice.getConsecutive();
        model.currency = invoice.getCurrency();
        model.total = invoice.getTotal();
        model.customerName = invoice.getCustomerName();
        model.lines = invoice.getItems().stream()
            .map(InvoiceXmlLine::from)
            .collect(Collectors.toList());
        model.taxes = invoice.getTaxes() == null ? List.of() : invoice.getTaxes().stream()
            .map(InvoiceXmlTax::from)
            .collect(Collectors.toList());
        return model;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class InvoiceXmlLine {
        private int lineNumber;
        private String description;
        private double quantity;
        private double unitPrice;
        private double subtotal;
        private double taxAmount;

        static InvoiceXmlLine from(InvoiceItem item) {
            InvoiceXmlLine line = new InvoiceXmlLine();
            line.lineNumber = item.getLineNumber();
            line.description = item.getDescription();
            line.quantity = item.getQuantity();
            line.unitPrice = item.getUnitPrice();
            line.subtotal = item.getSubtotal();
            line.taxAmount = item.getTaxAmount();
            return line;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class InvoiceXmlTax {
        private String taxType;
        private double taxRate;
        private double taxAmount;

        static InvoiceXmlTax from(InvoiceTax entity) {
            InvoiceXmlTax tax = new InvoiceXmlTax();
            tax.taxType = entity.getTaxType();
            tax.taxRate = entity.getTaxRate();
            tax.taxAmount = entity.getTaxAmount();
            return tax;
        }
    }
}
