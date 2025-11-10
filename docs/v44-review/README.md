# Revisión v4.4 de comprobantes electrónicos (Facturador CR)

Este paquete recopila el análisis técnico solicitado sobre la implementación PHP del facturador y su alineación con la normativa de comprobantes electrónicos v4.4 de Costa Rica.

## Contenido

- [`inventory.md`](inventory.md): Inventario de artefactos normativos y técnicos relevantes en el repositorio.
- [`field-map.md`](field-map.md): Mapeo de elementos obligatorios, opcionales y catálogos por tipo de comprobante.
- [`openapi-current.yaml`](openapi-current.yaml): Especificación OpenAPI 3.1 del contrato actual (`/api.php`) según los módulos cargados.
- [`openapi-proposed.yaml`](openapi-proposed.yaml): Contrato objetivo alineado a microservicio REST y a v4.4.
- [`examples/`](examples/): Cargas XML y JSON representativas generadas/ajustadas para v4.4.
- [`test-suite.md`](test-suite.md): Plan de pruebas unitarias e integración.
- [`checklist.md`](checklist.md): Lista auditable de cumplimiento v4.4.
- [`remediation-plan.md`](remediation-plan.md): Plan de remediación con prioridades P0/P1/P2.
- [`xsd/`](xsd/): Copias con formato legible de los XSD oficiales v4.4 incluidos en el repositorio.

> **Nota:** Las rutas de código citadas respetan el árbol original del repositorio (`api/`, `www/`, etc.). Las copias con formato (`xsd/*.pretty.xsd`) se añadieron únicamente para facilitar la lectura con números de línea estables.
