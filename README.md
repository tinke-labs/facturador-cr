# Facturador CR (Spring Boot)

Java Spring Boot microservice for Costa Rican electronic invoicing with multi-tenant API key authentication and PostgreSQL persistence.

## Features

- REST API for invoice submission, status retrieval, and retry (`/v1/invoices`).
- Tenant administration endpoint for certificate upload (`/v1/tenants/{id}/certificate`).
- Multi-tenant architecture with per-request API key resolution.
- XML generation scaffold using JAXB (version 4.4 payload template).
- Placeholder XAdES signing hook ready for integration with Apache Santuario / BouncyCastle.
- Hacienda client skeleton for token retrieval, submission, and status polling.
- Scheduler that refreshes invoice status for `SENT` documents.
- Flyway migrations for PostgreSQL schema.
- Docker Compose stack with PostgreSQL.

## Getting started

1. **Configure environment**
   ```bash
   cp application.yml.example application.yml
   ```
   Adjust datasource, Hacienda credentials, and encryption secret as needed.

2. **Run locally**
   ```bash
   mvn spring-boot:run
   ```

3. **Run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

4. **API Authentication**
   - Each request must include `X-Api-Key` with a tenant's API key.
   - Use the tenant controller to upload Base64 `.p12` certificates and PINs.

## Testing endpoints

Use the provided Postman collection (to be generated) or curl commands. Example invoice submission:

```bash
curl -X POST http://localhost:8080/v1/invoices \
  -H 'Content-Type: application/json' \
  -H 'X-Api-Key: <tenant-api-key>' \
  -d '{
    "documentType": "01",
    "currency": "CRC",
    "total": 1000,
    "customerName": "Cliente Demo",
    "items": [
      {
        "lineNumber": 1,
        "description": "Servicio",
        "quantity": 1,
        "unitPrice": 1000,
        "subtotal": 1000,
        "taxAmount": 0
      }
    ]
  }'
```

## Notes

- XML signing is provided as an integration point; replace the placeholder logic in `XmlSigner` with XAdES-EPES compliant implementation.
- `HaciendaClientImpl` uses `WebClient` and expects valid credentials/URLs for Hacienda's sandbox or production.
- Encryption secret must be a 16+ character string to derive the AES key for certificate PIN storage.
- Ensure tenants are preloaded with API keys and certificates before using the invoice endpoints.
