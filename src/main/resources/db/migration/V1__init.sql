CREATE TABLE tenants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    api_key VARCHAR(255) NOT NULL UNIQUE,
    certificate_inline TEXT,
    certificate_pin_encrypted VARCHAR(512),
    environment VARCHAR(32) NOT NULL,
    next_invoice_sequence BIGINT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE invoices (
    id SERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    clave VARCHAR(64) NOT NULL UNIQUE,
    consecutive VARCHAR(32) NOT NULL,
    document_type VARCHAR(32) NOT NULL,
    currency VARCHAR(8) NOT NULL,
    total NUMERIC(18,2) NOT NULL,
    customer_name VARCHAR(255),
    xml_path TEXT,
    status VARCHAR(32) NOT NULL,
    location_header VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE invoice_items (
    id SERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    line_number INTEGER NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity NUMERIC(18,5) NOT NULL,
    unit_price NUMERIC(18,5) NOT NULL,
    subtotal NUMERIC(18,5) NOT NULL,
    tax_amount NUMERIC(18,5) NOT NULL
);

CREATE TABLE invoice_taxes (
    id SERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    tax_type VARCHAR(64) NOT NULL,
    tax_rate NUMERIC(5,2) NOT NULL,
    tax_amount NUMERIC(18,5) NOT NULL
);

CREATE TABLE hacienda_responses (
    id SERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    status VARCHAR(64) NOT NULL,
    response_payload TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_invoices_tenant_status ON invoices(tenant_id, status);
CREATE INDEX idx_hacienda_responses_invoice ON hacienda_responses(invoice_id);
