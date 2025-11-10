# Inventario v4.4 en el repositorio

| Tipo | Nombre | Ruta | Fuente |
| ---- | ------ | ---- | ------ |
| XSD | FacturaElectronica | `www/xsd/FacturaElectronica_V4.4.xsd` | Estructura oficial v4.4, incluye claves, emisor, receptor, detalle y resumen. 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L1-L120】 |
| XSD | FacturaElectronica (sin firma) | `www/xsd/FacturaElectronica_V4.4-noSign.xsd` | Variante para validación previa al firmado digital. 【F:docs/v44-review/xsd/FacturaElectronica_V4.4-noSign.pretty.xsd†L1-L60】 |
| XSD | FacturaElectronicaCompra | `www/xsd/FacturaElectronicaCompra_V4.4.xsd` | Definición v4.4 para compras. 【F:docs/v44-review/xsd/FacturaElectronicaCompra_V4.4.pretty.xsd†L1-L110】 |
| XSD | FacturaElectronicaExportacion | `www/xsd/FacturaElectronicaExportacion_V4.4.xsd` | Definición de exportación. 【F:docs/v44-review/xsd/FacturaElectronicaExportacion_V4.4.pretty.xsd†L1-L120】 |
| XSD | TiqueteElectronico | `www/xsd/TiqueteElectronico_V4.4.xsd` | Definición oficial para tiquete. 【F:docs/v44-review/xsd/TiqueteElectronico_V4.4.pretty.xsd†L1-L110】 |
| XSD | NotaCreditoElectronica | `www/xsd/NotaCreditoElectronica_V4.4.xsd` | Definición para notas de crédito. 【F:docs/v44-review/xsd/NotaCreditoElectronica_V4.4.pretty.xsd†L1-L110】 |
| XSD | NotaDebitoElectronica | `www/xsd/NotaDebitoElectronica_V4.4.xsd` | Definición para notas de débito. 【F:docs/v44-review/xsd/NotaDebitoElectronica_V4.4.pretty.xsd†L1-L110】 |
| XSD | MensajeReceptor | `www/xsd/MensajeReceptor_V4.4.xsd` | Mensaje de aceptación/rechazo. 【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L1-L90】 |
| XSD | MensajeHacienda | `www/xsd/MensajeHacienda_V4.4.xsd` | Respuestas oficiales del Ministerio. 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L1-L70】 |
| XSD | ReciboElectronicoPago | `www/xsd/ReciboElectronicoPago_V4.4.xsd` | Recibos electrónicos de pago. 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L1-L100】 |
| JSON | Colección Postman | `recursos/API CRLibre.postman_collection V3.1.json` | Endpoints preconfigurados (referencias previas). 【F:recursos/API CRLibre.postman_collection V3.1.json†L1-L40】 |
| SQL | Tablas para Facturador | `recursos/Tablas para Facturador CRLibre.sql` | Estructuras de BD relevantes para comprobantes. 【F:recursos/Tablas para Facturador CRLibre.sql†L1-L80】 |
| ZIP | Paquetes base | `api/install/cala_base.zip` | Instalador (no contiene docs v4.4). |
| ZIP | Paquetes base dev | `api/install/cala_base_contentz_dev.zip` | Variación de instalador. |

## Otros artefactos por versión

No se encontraron documentos v4.3 en el repositorio; el árbol actual ya apunta a la rama `v.4.4`. Los intentos de descargar directamente los anexos oficiales 2024 desde Hacienda devolvieron HTTP 403 (bloqueo en este entorno). 【a5aa3d†L1-L10】
