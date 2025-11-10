# Checklist de cumplimiento v4.4

| Regla / Campo | Fuente | Estado | Tarea pendiente | Responsable | ETA |
| --- | --- | --- | --- | --- | --- |
| Incluir `ProveedorSistemas` en todos los XML | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L40-L48】 | No | Ajustar generadores y DTOs para exponer parámetro. | Backend | 1 sprint |
| Validar padding de `CodigoActividad` (emisor/receptor) | 【F:api/contrib/genXML/genXML.php†L118-L136】 | Parcial | Añadir pruebas unitarias y normalizar inputs en UI. | Backend / Front | 1 sprint |
| Soportar detalle surtido (paquetes/combo) | 【F:docs/v44-review/xsd/FacturaElectronica_V4.4.pretty.xsd†L260-L352】 | No | Extender UI y generador para capturar componentes de combos. | Producto | 2 sprints |
| Implementar ReciboElectricoPago v4.4 | 【F:docs/v44-review/xsd/ReciboElectronicoPago_V4.4.pretty.xsd†L96-L460】 | No | Crear generador, validaciones y pruebas. | Backend | 2 sprints |
| Parsear MensajeHacienda completo | 【F:docs/v44-review/xsd/MensajeHacienda_V4.4.pretty.xsd†L20-L200】 | No | Implementar parser/DTO y almacenamiento de estado. | Backend | 1 sprint |
| Seguridad de certificados (no almacenar PIN plano) | 【F:api/contrib/signXML/Firmadohaciendacr.php†L40-L64】 | No | Encriptar PIN en repositorio seguro y rotación automática. | DevOps | 1 sprint |
| Habilitar verificación SSL en envíos | 【F:api/contrib/send/send.php†L40-L66】 | No | Eliminar `CURLOPT_SSL_VERIFYHOST/PEER = false` y manejar CA. | Backend | 0.5 sprint |
| Gestión multi-tenant de archivos y colas | 【F:api/contrib/crlibreall/module.php†L24-L120】 | Parcial | Aislar rutas y credenciales por emisor; definir colas por empresa. | Arquitectura | 2 sprints |
