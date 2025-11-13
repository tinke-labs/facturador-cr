package com.facturador.xml;

import com.facturador.models.invoice.Invoice;
import com.facturador.models.invoice.InvoiceItem;
import com.facturador.models.invoice.InvoiceTax;
import com.facturador.models.tenant.Tenant;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "FacturaElectronica")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "clave",
    "codigoActividad",
    "numeroConsecutivo",
    "fechaEmision",
    "emisor",
    "receptor",
    "condicionVenta",
    "plazoCredito",
    "medioPago",
    "detalleServicio",
    "resumenFactura"
})
public class InvoiceXmlModel {

    private static final DateTimeFormatter EMISSION_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    @XmlElement(name = "Clave", required = true)
    private String clave;

    @XmlElement(name = "CodigoActividad", required = true)
    private String codigoActividad;

    @XmlElement(name = "NumeroConsecutivo", required = true)
    private String numeroConsecutivo;

    @XmlElement(name = "FechaEmision", required = true)
    private String fechaEmision;

    @XmlElement(name = "Emisor", required = true)
    private PartyXml emisor;

    @XmlElement(name = "Receptor")
    private PartyXml receptor;

    @XmlElement(name = "CondicionVenta", required = true)
    private String condicionVenta;

    @XmlElement(name = "PlazoCredito")
    private String plazoCredito;

    @XmlElement(name = "MedioPago", required = true)
    private List<String> medioPago = new ArrayList<>();

    @XmlElement(name = "DetalleServicio", required = true)
    private DetailXml detalleServicio;

    @XmlElement(name = "ResumenFactura", required = true)
    private SummaryXml resumenFactura;

    public static InvoiceXmlModel from(Invoice invoice) {
        InvoiceXmlModel model = new InvoiceXmlModel();
        Tenant tenant = invoice.getTenant();
        model.clave = invoice.getClave();
        model.codigoActividad = invoice.getActivityCode();
        model.numeroConsecutivo = invoice.getConsecutive();
        model.fechaEmision = invoice.getIssueDate().format(EMISSION_FORMATTER);
        model.emisor = PartyXml.fromTenant(tenant);
        model.receptor = PartyXml.fromInvoice(invoice);
        model.condicionVenta = invoice.getSaleCondition();
        model.plazoCredito = invoice.getCreditTerm();
        model.medioPago = invoice.getPaymentMethods() == null
            ? new ArrayList<>()
            : new ArrayList<>(invoice.getPaymentMethods());
        if (model.medioPago.isEmpty()) {
            throw new IllegalStateException("Invoice must include at least one payment method");
        }
        model.detalleServicio = DetailXml.from(invoice.getItems());
        model.resumenFactura = SummaryXml.from(invoice);
        return model;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PartyXml {

        @XmlElement(name = "Nombre", required = true)
        private String nombre;

        @XmlElement(name = "Identificacion")
        private IdentificationXml identificacion;

        @XmlElement(name = "NombreComercial")
        private String nombreComercial;

        @XmlElement(name = "Ubicacion")
        private LocationXml ubicacion;

        @XmlElement(name = "Telefono")
        private PhoneXml telefono;

        @XmlElement(name = "CorreoElectronico")
        private String correoElectronico;

        static PartyXml fromTenant(Tenant tenant) {
            PartyXml xml = new PartyXml();
            xml.nombre = tenant.getName();
            if (tenant.getIdentificationType() == null || tenant.getIdentificationNumber() == null) {
                throw new IllegalStateException("Tenant missing identification for XML emission");
            }
            xml.identificacion = new IdentificationXml(tenant.getIdentificationType(), tenant.getIdentificationNumber());
            xml.nombreComercial = tenant.getCommercialName();
            if (tenant.getProvince() == null || tenant.getCanton() == null || tenant.getDistrict() == null || tenant.getOtherSigns() == null) {
                throw new IllegalStateException("Tenant missing location data for XML emission");
            }
            xml.ubicacion = new LocationXml(
                tenant.getProvince(),
                tenant.getCanton(),
                tenant.getDistrict(),
                tenant.getNeighborhood(),
                tenant.getOtherSigns()
            );
            if (tenant.getPhoneCountryCode() != null && tenant.getPhoneNumber() != null) {
                xml.telefono = new PhoneXml(tenant.getPhoneCountryCode(), tenant.getPhoneNumber());
            }
            xml.correoElectronico = tenant.getEmail();
            return xml;
        }

        static PartyXml fromInvoice(Invoice invoice) {
            if (invoice.getCustomerName() == null || invoice.getCustomerName().isBlank()) {
                return null;
            }
            PartyXml xml = new PartyXml();
            xml.nombre = invoice.getCustomerName();
            if (invoice.getReceiverIdentificationType() != null && invoice.getReceiverIdentificationNumber() != null) {
                xml.identificacion = new IdentificationXml(
                    invoice.getReceiverIdentificationType(),
                    invoice.getReceiverIdentificationNumber()
                );
            }
            xml.nombreComercial = invoice.getReceiverCommercialName();
            if (invoice.getReceiverProvince() != null) {
                xml.ubicacion = new LocationXml(
                    invoice.getReceiverProvince(),
                    invoice.getReceiverCanton(),
                    invoice.getReceiverDistrict(),
                    invoice.getReceiverNeighborhood(),
                    invoice.getReceiverOtherSigns()
                );
            }
            if (invoice.getReceiverPhoneCountry() != null && invoice.getReceiverPhoneNumber() != null) {
                xml.telefono = new PhoneXml(invoice.getReceiverPhoneCountry(), invoice.getReceiverPhoneNumber());
            }
            xml.correoElectronico = invoice.getReceiverEmail();
            return xml;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class IdentificationXml {

        @XmlElement(name = "Tipo", required = true)
        private String tipo;

        @XmlElement(name = "Numero", required = true)
        private String numero;

        IdentificationXml() {
        }

        IdentificationXml(String tipo, String numero) {
            this.tipo = tipo;
            this.numero = numero;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LocationXml {

        @XmlElement(name = "Provincia", required = true)
        private String provincia;

        @XmlElement(name = "Canton", required = true)
        private String canton;

        @XmlElement(name = "Distrito", required = true)
        private String distrito;

        @XmlElement(name = "Barrio")
        private String barrio;

        @XmlElement(name = "OtrasSenas", required = true)
        private String otrasSenas;

        LocationXml() {
        }

        LocationXml(String provincia, String canton, String distrito, String barrio, String otrasSenas) {
            this.provincia = provincia;
            this.canton = canton;
            this.distrito = distrito;
            this.barrio = barrio;
            this.otrasSenas = otrasSenas;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PhoneXml {

        @XmlElement(name = "CodigoPais", required = true)
        private String codigoPais;

        @XmlElement(name = "NumTelefono", required = true)
        private String numTelefono;

        PhoneXml() {
        }

        PhoneXml(String codigoPais, String numTelefono) {
            this.codigoPais = codigoPais;
            this.numTelefono = numTelefono;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DetailXml {

        @XmlElement(name = "LineaDetalle", required = true)
        private List<LineDetailXml> lineaDetalle = new ArrayList<>();

        static DetailXml from(List<InvoiceItem> items) {
            DetailXml detailXml = new DetailXml();
            if (items == null) {
                return detailXml;
            }
            for (InvoiceItem item : items) {
                detailXml.lineaDetalle.add(LineDetailXml.from(item));
            }
            return detailXml;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LineDetailXml {

        @XmlElement(name = "NumeroLinea", required = true)
        private Integer numeroLinea;

        @XmlElement(name = "Codigo")
        private CodeXml codigo;

        @XmlElement(name = "CodigoComercial")
        private CommercialCodeXml codigoComercial;

        @XmlElement(name = "Cantidad", required = true)
        private BigDecimal cantidad;

        @XmlElement(name = "UnidadMedida", required = true)
        private String unidadMedida;

        @XmlElement(name = "UnidadMedidaComercial")
        private String unidadMedidaComercial;

        @XmlElement(name = "Detalle", required = true)
        private String detalle;

        @XmlElement(name = "PrecioUnitario", required = true)
        private BigDecimal precioUnitario;

        @XmlElement(name = "MontoTotal", required = true)
        private BigDecimal montoTotal;

        @XmlElement(name = "Descuento")
        private DiscountXml descuento;

        @XmlElement(name = "SubTotal", required = true)
        private BigDecimal subTotal;

        @XmlElement(name = "BaseImponible")
        private BigDecimal baseImponible;

        @XmlElement(name = "Impuesto")
        private List<TaxXml> impuesto;

        @XmlElement(name = "MontoTotalLinea", required = true)
        private BigDecimal montoTotalLinea;

        static LineDetailXml from(InvoiceItem item) {
            LineDetailXml xml = new LineDetailXml();
            xml.numeroLinea = item.getLineNumber();
            xml.codigo = new CodeXml("01", item.getCommercialCode());
            xml.codigoComercial = new CommercialCodeXml("01", item.getCommercialCode());
            xml.cantidad = item.getQuantity();
            xml.unidadMedida = item.getMeasurementUnit();
            xml.unidadMedidaComercial = item.getCommercialUnit();
            xml.detalle = item.getDescription();
            xml.precioUnitario = item.getUnitPrice();
            xml.montoTotal = item.getTotalAmount();
            if (item.getDiscountAmount() != null && item.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
                xml.descuento = new DiscountXml(item.getDiscountAmount(), item.getDiscountReason());
            }
            xml.subTotal = item.getSubtotal();
            xml.baseImponible = item.getTaxableBase();
            if (item.getTaxes() != null && !item.getTaxes().isEmpty()) {
                xml.impuesto = new ArrayList<>();
                for (InvoiceTax tax : item.getTaxes()) {
                    xml.impuesto.add(TaxXml.from(tax));
                }
            }
            xml.montoTotalLinea = item.getTotalLineAmount();
            return xml;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CommercialCodeXml {

        @XmlElement(name = "Tipo", required = true)
        private String tipo;

        @XmlElement(name = "Codigo", required = true)
        private String codigo;

        CommercialCodeXml() {
        }

        CommercialCodeXml(String tipo, String codigo) {
            this.tipo = tipo;
            this.codigo = codigo;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CodeXml {

        @XmlElement(name = "Tipo", required = true)
        private String tipo;

        @XmlElement(name = "Codigo", required = true)
        private String codigo;

        CodeXml() {
        }

        CodeXml(String tipo, String codigo) {
            this.tipo = tipo;
            this.codigo = codigo;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DiscountXml {

        @XmlElement(name = "MontoDescuento", required = true)
        private BigDecimal montoDescuento;

        @XmlElement(name = "NaturalezaDescuento", required = true)
        private String naturalezaDescuento;

        DiscountXml() {
        }

        DiscountXml(BigDecimal montoDescuento, String naturalezaDescuento) {
            this.montoDescuento = montoDescuento;
            this.naturalezaDescuento = naturalezaDescuento == null ? "DESCUENTO" : naturalezaDescuento;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TaxXml {

        @XmlElement(name = "Codigo", required = true)
        private String codigo;

        @XmlElement(name = "CodigoTarifa")
        private String codigoTarifa;

        @XmlElement(name = "Tarifa", required = true)
        private BigDecimal tarifa;

        @XmlElement(name = "FactorIVA")
        private BigDecimal factorIVA;

        @XmlElement(name = "BaseImponible", required = true)
        private BigDecimal baseImponible;

        @XmlElement(name = "Monto", required = true)
        private BigDecimal monto;

        static TaxXml from(InvoiceTax tax) {
            TaxXml xml = new TaxXml();
            xml.codigo = tax.getTaxCode();
            xml.codigoTarifa = tax.getRateCode();
            xml.tarifa = tax.getTaxRate();
            xml.factorIVA = tax.getFactorIVA();
            xml.baseImponible = tax.getTaxableBase();
            xml.monto = tax.getTaxAmount();
            return xml;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SummaryXml {

        @XmlElement(name = "CodigoTipoMoneda")
        private CurrencyXml codigoTipoMoneda;

        @XmlElement(name = "TotalServGravados", required = true)
        private BigDecimal totalServGravados;

        @XmlElement(name = "TotalServExentos", required = true)
        private BigDecimal totalServExentos;

        @XmlElement(name = "TotalMercanciasGravadas", required = true)
        private BigDecimal totalMercanciasGravadas;

        @XmlElement(name = "TotalMercanciasExentas", required = true)
        private BigDecimal totalMercanciasExentas;

        @XmlElement(name = "TotalGravado", required = true)
        private BigDecimal totalGravado;

        @XmlElement(name = "TotalExento", required = true)
        private BigDecimal totalExento;

        @XmlElement(name = "TotalVenta", required = true)
        private BigDecimal totalVenta;

        @XmlElement(name = "TotalDescuentos", required = true)
        private BigDecimal totalDescuentos;

        @XmlElement(name = "TotalVentaNeta", required = true)
        private BigDecimal totalVentaNeta;

        @XmlElement(name = "TotalImpuesto", required = true)
        private BigDecimal totalImpuesto;

        @XmlElement(name = "TotalIVADevuelto", required = true)
        private BigDecimal totalIVADevuelto;

        @XmlElement(name = "TotalOtrosCargos", required = true)
        private BigDecimal totalOtrosCargos;

        @XmlElement(name = "TotalComprobante", required = true)
        private BigDecimal totalComprobante;

        static SummaryXml from(Invoice invoice) {
            SummaryXml xml = new SummaryXml();
            if (invoice.getExchangeRate() != null) {
                xml.codigoTipoMoneda = new CurrencyXml(invoice.getCurrency(), invoice.getExchangeRate());
            }
            xml.totalServGravados = defaultZero(invoice.getSummaryTotalTaxedServices());
            xml.totalServExentos = defaultZero(invoice.getSummaryTotalExemptServices());
            xml.totalMercanciasGravadas = defaultZero(invoice.getSummaryTotalTaxedGoods());
            xml.totalMercanciasExentas = defaultZero(invoice.getSummaryTotalExemptGoods());
            xml.totalGravado = defaultZero(invoice.getSummaryTotalTaxed());
            xml.totalExento = defaultZero(invoice.getSummaryTotalExempt());
            xml.totalVenta = defaultZero(invoice.getSummaryTotalSale());
            xml.totalDescuentos = defaultZero(invoice.getSummaryTotalDiscounts());
            xml.totalVentaNeta = defaultZero(invoice.getSummaryTotalNetSale());
            xml.totalImpuesto = defaultZero(invoice.getSummaryTotalTax());
            xml.totalIVADevuelto = defaultZero(invoice.getSummaryTotalIvaRefund());
            xml.totalOtrosCargos = defaultZero(invoice.getSummaryTotalOtherCharges());
            xml.totalComprobante = defaultZero(invoice.getSummaryTotalVoucher());
            return xml;
        }

        private static BigDecimal defaultZero(BigDecimal value) {
            return value == null ? BigDecimal.ZERO : value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CurrencyXml {

        @XmlElement(name = "CodigoMoneda", required = true)
        private String codigoMoneda;

        @XmlElement(name = "TipoCambio", required = true)
        private BigDecimal tipoCambio;

        CurrencyXml() {
        }

        CurrencyXml(String codigoMoneda, BigDecimal tipoCambio) {
            this.codigoMoneda = codigoMoneda;
            this.tipoCambio = tipoCambio;
        }
    }
}
