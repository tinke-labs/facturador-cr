# Plan de remediación

| Prioridad | Tarea | Alcance | Referencia | Estimación |
| --- | --- | --- | --- | --- |
| P0 | Implementar generador ReciboElectricoPago (`genXMLRep`) con validación XSD y envío | Extender `api/contrib/genXML` y `crlibreall` para nueva ruta, actualizar `scripts/generate_v44_examples.php`. | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L96-L460】【F:api/contrib/crlibreall/module.php†L24-L120】 | 8 pts |
| P0 | Integrar MensajeHacienda en pipeline (parsing + almacenamiento) | Crear DTO, tabla y consumidor para mensajes de Hacienda. | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L20-L200】【F:api/contrib/send/consultar.php†L24-L56】 | 5 pts |
| P0 | Endurecer seguridad de certificados (PIN, SSL) | Cifrar PIN, usar almacenamiento seguro, habilitar verificación TLS. | 【F:api/contrib/signXML/Firmadohaciendacr.php†L40-L64】【F:api/contrib/send/send.php†L40-L66】 | 5 pts |
| P1 | Refactorizar `scripts/generate_v44_examples.php` para cobertura total (REP y MH) | Completar validaciones faltantes y publicar fixtures. | 【F:scripts/generate_v44_examples.php†L1-L220】 | 3 pts |
| P1 | Actualizar OpenAPI actual (contrato `/api.php`) | Revisar especificación `openapi-current.yaml` para cubrir todos los módulos expuestos. | 【F:docs/v44-review/openapi-current.yaml†L1-L200】 | 2 pts |
| P1 | Diseñar capa de colas e idempotencia | Integrar almacenamiento de jobs por emisor y reintentos configurables. | 【F:api/contrib/send/send.php†L17-L108】 | 8 pts |
| P2 | Documentar catálogos (CABYS, monedas, impuestos) en UI y API | Exponer endpoints/seeders para catálogos oficiales. | 【F:docs/v44-review/field-map.md†L6-L80】 | 3 pts |
| P2 | Automatizar pruebas de regresión (`phpunit`) | Cubrir generadores y firmas, integrando fixtures. | 【F:tests/api_contrib_genXML_FE.php†L1-L160】 | 5 pts |
