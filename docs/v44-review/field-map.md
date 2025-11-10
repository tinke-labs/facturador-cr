# Mapa de campos y reglas v4.4

## FacturaElectronica

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| Clave | Sí | Cadena fija de 50 dígitos usada para control fiscal. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L24-L40】 |
| CodigoActividadEmisor | Sí | String 6 dígitos, se rellena con ceros. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L42-L66】 |
| CodigoActividadReceptor | Condicional | 6 dígitos cuando aplica crédito o gasto deducible. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L66-L92】 |
| Emisor.Nombre / Identificacion | Sí | Nombre ≤100 caracteres, tipo 01-05, número según cédula. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L108-L168】 |
| Emisor.Ubicacion | Condicional | Provincia/Cantón/Distrito/OtrasSenas obligatorios juntos. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L168-L216】 |
| CondicionVenta | Sí | Enumeración 01-11 y 99 (otros, requiere descripción). | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L190-L238】 |
| MediosPago | 1-4 registros | TipoMedioPago enumerado 01-07 y 99, con descripción cuando 99. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L620-L672】 |
| LineaDetalle | Sí (1-1000) | Incluye CodigoCABYS (13 dígitos), Cantidad decimal (16,3), Descuento, Impuesto. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L238-L520】 |
| LineaDetalle.DetalleSurtido | Condicional | Máximo 20 líneas por combo, cada una con CABYS y códigos surtido. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L260-L352】 |
| Impuesto | Condicional | Códigos 01-13, tarifas IVA según catálogo, soporte para exoneraciones. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L356-L454】 |
| ResumenFactura | Sí | Totales gravados/exentos/no sujeto, TotalImpuesto, TotalComprobante. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L520-L640】 |
| OtrosCargos | Máx 15 | Tipos 01-10, monto y porcentaje opcional, requiere detalle. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L562-L610】 |
| InformacionReferencia | Opcional (0-40) | TipoDoc enumerado (01,02,04,05,99), requiere fecha y razón cuando 99. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L672-L760】 |

## FacturaElectronicaCompra

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| Clave / NumeroConsecutivo | Sí | Igual que Factura estándar, conserva longitud y formato. | 【F:docs/v44-review/xsd/FacturaElectronicaCompra_V4.4.pretty.xsd†L20-L80】 |
| CodigoActividadEmisor | Sí | 6 dígitos; para receptor se usa CodigoActividadReceptor condicional. | 【F:docs/v44-review/xsd/FacturaElectronicaCompra_V4.4.pretty.xsd†L80-L126】 |
| DetalleServicio | Sí | Estructura heredada de Factura con soporte surtido y exoneraciones. | 【F:docs/v44-review/xsd/FacturaElectronicaCompra_V4.4.pretty.xsd†L214-L430】 |
| ResumenFactura | Sí | Incluye TotalIVADevuelto y TotalImpAsumEmisorFabrica para compras. | 【F:docs/v44-review/xsd/FacturaElectronicaCompra_V4.4.pretty.xsd†L472-L594】 |
| InformacionReferencia | Opcional | Igual a factura, máximo 40 referencias. | 【F:docs/v44-review/xsd/FacturaElectronicaCompra_V4.4.pretty.xsd†L630-L704】 |

## FacturaElectronicaExportacion

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| Emisor / Receptor | Sí | Receptor acepta dirección extranjera (`OtrasSenasExtranjero`). | 【F:docs/v44-review/xsd/FacturaElectronicaExportacion_V4.4.pretty.xsd†L120-L200】 |
| CondicionVenta | Sí | Igual catálogo que Factura, pero exige detalle de transporte internacional. | 【F:docs/v44-review/xsd/FacturaElectronicaExportacion_V4.4.pretty.xsd†L202-L258】 |
| DetalleServicio | Sí | Mantiene CABYS y surtidos; exportaciones permiten múltiples Incoterms vía otros. | 【F:docs/v44-review/xsd/FacturaElectronicaExportacion_V4.4.pretty.xsd†L260-L420】 |
| ResumenFactura | Sí | Misma estructura con totales convertidos según CódigoTipoMoneda. | 【F:docs/v44-review/xsd/FacturaElectronicaExportacion_V4.4.pretty.xsd†L470-L592】 |

## NotaCreditoElectronica / NotaDebitoElectronica

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| Clave / CodigoActividad | Sí | Igual que factura, utiliza ProveedorSistemas y Registrofiscal8707. | 【F:docs/v44-review/xsd/NotaCreditoElectronica_V4.4.pretty.xsd†L20-L120】 |
| CondicionVenta / MediosPago | Condicional | Solo cuando hay devolución de dinero o ajustes financieros. | 【F:docs/v44-review/xsd/NotaCreditoElectronica_V4.4.pretty.xsd†L182-L248】 |
| DetalleServicio | Sí | Permite indicar tipo de documento de referencia por línea. | 【F:docs/v44-review/xsd/NotaCreditoElectronica_V4.4.pretty.xsd†L248-L440】 |
| InformacionReferencia | Sí (1-40) | Necesaria para anular/corregir documentos originales. | 【F:docs/v44-review/xsd/NotaCreditoElectronica_V4.4.pretty.xsd†L520-L612】 |

## TiqueteElectronico

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| Receptor | Opcional | Se puede omitir cédula/dirección cuando es consumidor final. | 【F:docs/v44-review/xsd/TiqueteElectronico_V4.4.pretty.xsd†L140-L220】 |
| DetalleServicio | Sí | Misma estructura que factura; número de líneas máximo 1000. | 【F:docs/v44-review/xsd/TiqueteElectronico_V4.4.pretty.xsd†L220-L420】 |
| ResumenFactura | Sí | Similar, pero TotalOtrosCargos solo para recargos autorizados. | 【F:docs/v44-review/xsd/TiqueteElectronico_V4.4.pretty.xsd†L460-L550】 |

## MensajeReceptor

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| NumeroCedulaEmisor/Receptor | Sí | 12 dígitos, se rellena con ceros a la izquierda. | 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L20-L80】 |
| FechaEmisionDoc | Sí | Fecha/hora ISO 8601 de la aceptación/rechazo. | 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L84-L116】 |
| Mensaje | Sí | Enumeración 1 (aceptado), 2 (aceptado parcial), 3 (rechazado). | 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L120-L156】 |
| CodigoActividad | Sí | Código económico del receptor para compras deducibles (6 dígitos). | 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L160-L196】 |
| TotalFactura | Sí | Monto total del comprobante original. | 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L200-L232】 |

## ReciboElectronicoPago

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| CondicionVenta | Sí | Solo admite códigos 09 y 11 según Hacienda. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L96-L140】 |
| DetalleServicio.LineaDetalle | Sí | Incluye SubTotal editable y cálculo de IVA asociado al pago. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L200-L320】 |
| ResumenFactura | Sí | Debe reportar CódigoTipoMoneda, TotalVenta, TotalVentaNeta y MediosPago. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L360-L460】 |
| InformacionReferencia | Opcional (0-10) | Usa elementos TipoDocIR, FechaEmisionIR, Codigo, Razon. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L720-L812】 |

## MensajeHacienda

| Elemento | Obligatorio | Tipo / Restricción | Referencia |
| --- | --- | --- | --- |
| NombreEmisor/TipoIdentificacionEmisor | Sí | Razón social y tipo 01-06. | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L20-L100】 |
| NombreReceptor/TipoIdentificacionReceptor | Sí | Datos del receptor según catálogos oficiales. | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L100-L160】 |
| Mensaje | Sí | 1 = aceptado, 3 = rechazado. | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L400-L420】 |
| EstadoMensaje | Sí | Texto hasta 9 caracteres que describe el estado (ej. `procesado`). | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L468-L480】 |
| DetalleMensaje | Condicional | Observaciones libres, útil para rechazos o advertencias. | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L480-L500】 |

