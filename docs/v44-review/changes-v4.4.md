# Cambios relevantes de v4.3 a v4.4

| Componente | Cambio clave | Impacto | Referencia |
| --- | --- | --- | --- |
| ProveedorSistemas | Nuevo nodo requerido en todos los comprobantes principales. | Se debe parametrizar el código del proveedor al generar XML. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L40-L48】 |
| CodigoActividadReceptor | Campo condicional para crédito fiscal, se añade a Factura y notas. | Requiere capturar actividad económica del receptor. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L66-L92】 |
| DetalleSurtido | Nuevo bloque para combos/surtidos (máx. 20 elementos). | Validaciones adicionales para CABYS y unidades por componente. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L260-L352】 |
| TotalDesgloseImpuesto | Se incorpora desglose por código/tarifa en Resumen. | Ajustar cálculos y almacenamiento por tipo de impuesto. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L560-L620】 |
| Registrofiscal8707 | Campo opcional para emisor cuando aplica artículo 87.07. | Se debe exponer en UI/servicio y almacenar. | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L120-L136】 |
| MensajeReceptor | Se exige CodigoActividad (6 dígitos) y TotalFactura. | Validaciones extra al generar aceptación/rechazo. | 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L160-L232】 |
| ReciboElectronicoPago | Nuevo comprobante con subtotal editable y medio de pago. | Implementar generador y validaciones específicas. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L96-L460】 |
| MensajeHacienda | Estructura completa para respuestas oficiales (nombre emisor/receptor, mensaje, estado). | Parser debe ajustarse para leer nuevos campos. | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L20-L200】【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L400-L500】 |

> Nota: La documentación oficial 2024 publicada por Hacienda retornó HTTP 403 en este entorno (ver intento con `curl`). 【a5aa3d†L1-L10】
