# Plan de pruebas propuesto

## Validación estructural

| Caso | Descripción | Implementación sugerida | Referencia |
| --- | --- | --- | --- |
| Validar XML contra XSD v4.4 | Ejecutar `DOMDocument::schemaValidate` para cada comprobante (factura, notas, tiquete, compra, exportación, MR, REP, mensaje Hacienda). | Crear comando `php scripts/generate_v44_examples.php` y pruebas automatizadas que usen `www/xsd/*.xsd` oficiales. | 【F:scripts/generate_v44_examples.php†L1-L120】【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L24-L520】 |
| Validar firma XMLDSig | Usar `api/contrib/firmarXML` para firmar y luego validar con `xmldsig-core-schema.xsd`. | Adaptar `scripts/generate_v44_examples.php` para invocar `Firmadohaciendacr::firmarXML`. | 【F:api/contrib/firmarXML/README.md†L1-L38】【F:api/contrib/signXML/Firmadohaciendacr.php†L250-L340】 |

## Reglas normativas

| Caso | Regla | Cobertura | Referencia |
| --- | --- | --- | --- |
| Condición de venta 99 con descripción | Si `CondicionVenta=99` debe existir `CondicionVentaOtros`. | Agregar aserción en generadores (`genXMLFe`, `genXMLNC`, etc.). | 【F:api/contrib/genXML/genXML.php†L188-L220】 |
| Medios de pago máx. 4 y descripción cuando 99 | Controlar longitud y texto. | Ya se limita en generador pero falta prueba unitaria dedicada. | 【F:api/contrib/genXML/genXML.php†L206-L218】【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L620-L672】 |
| CodigoActividad padding | Debe ser de 6 dígitos para emisor/receptor/MR. | Asegurar pruebas que fallen si el padding se omite. | 【F:api/contrib/genXML/genXML.php†L118-L180】【F:docs/v44-review/xsd/MensajeReceptor_V4.4.pretty.xsd†L156-L200】 |
| ReciboElectricoPago subtotal editable | Verificar que el subTotal corresponde a pagos parciales y recalcula IVA. | Crear fixture comparando `SubTotal`, `ImpuestoNeto`, `MontoTotalLinea`. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L200-L320】 |

## Flujos de emisión

| Flujo | Pasos | Observaciones | Referencia |
| --- | --- | --- | --- |
| Emisión completa | 1) Generar clave (`api/contrib/clave`) 2) Crear XML (`genXML`) 3) Firmar (`signXML`) 4) Enviar (`contrib/send`). | Actualizar pruebas end-to-end para sandbox y producción (deshabilitar `CURLOPT_SSL_VERIFYPEER=false`). | 【F:api/contrib/clave/module.php†L24-L44】【F:api/contrib/genXML/module.php†L46-L520】【F:api/contrib/signXML/module.php†L24-L40】【F:api/contrib/send/send.php†L17-L108】 |
| Mensaje receptor | 1) Construir MR (`genXMLMr`) 2) Firmar 3) Enviar (`sendMensaje`). | Validar consecutivo de receptor (20 dígitos) y respuesta de Hacienda. | 【F:api/contrib/genXML/module.php†L337-L410】【F:api/contrib/send/send.php†L70-L108】 |
| Consulta estado | Consumir API `/recepcion/v1/recepcion/` vía módulo `consultar`. | Debe manejar reintentos, estados 202, 400, 401. | 【F:api/contrib/consultar/consultar.php†L24-L56】 |

## Reintentos e idempotencia

| Caso | Objetivo | Implementación | Referencia |
| --- | --- | --- | --- |
| Reintento envío Hacienda | Reintentar en 5xx con backoff exponencial. | Envolver `curl_exec` en `send.php` con `retry` y logging. | 【F:api/contrib/send/send.php†L17-L108】 |
| Gestión de colas | Garantizar orden emisión/recepción por compañía. | Integrar cola (RabbitMQ/Redis) y jobs para `send`/`sendMensaje`. | 【F:api/contrib/crlibreall/module.php†L24-L120】 |

## Fixtures

| Fixture | Contenido | Uso | Referencia |
| --- | --- | --- | --- |
| `docs/v44-review/examples/*.xml` | XML base64 generado (falta completar REP y MH con validación final). | Alimentar pruebas de validación y firma. | 【F:scripts/generate_v44_examples.php†L1-L180】【F:docs/v44-review/examples/factura-electronica.xml†L1-L80】 |
| Datos SQL base | Tablas de facturador (clientes, productos, consecutivos). | Preparar datasets de integración. | 【F:recursos/Tablas para Facturador CRLibre.sql†L1-L120】 |
