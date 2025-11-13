CREATE TABLE tenants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    api_key VARCHAR(255) NOT NULL UNIQUE,
    identification_type VARCHAR(2) NOT NULL,
    identification_number VARCHAR(20) NOT NULL,
    commercial_name VARCHAR(255),
    email VARCHAR(255),
    branch_code VARCHAR(3) NOT NULL,
    terminal_code VARCHAR(5) NOT NULL,
    situation VARCHAR(1) NOT NULL,
    phone_country_code VARCHAR(4),
    phone_number VARCHAR(20),
    province VARCHAR(1),
    canton VARCHAR(2),
    district VARCHAR(2),
    neighborhood VARCHAR(2),
    other_signs TEXT,
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
    activity_code VARCHAR(6) NOT NULL,
    consecutive VARCHAR(32) NOT NULL,
    issue_date TIMESTAMPTZ NOT NULL,
    document_type VARCHAR(32) NOT NULL,
    sale_condition VARCHAR(32) NOT NULL,
    credit_term VARCHAR(10),
    currency VARCHAR(8) NOT NULL,
    exchange_rate NUMERIC(18,5),
    payment_methods TEXT,
    total NUMERIC(18,5) NOT NULL,
    customer_name VARCHAR(255),
    receiver_identification_type VARCHAR(2),
    receiver_identification_number VARCHAR(20),
    receiver_commercial_name VARCHAR(255),
    receiver_province VARCHAR(1),
    receiver_canton VARCHAR(2),
    receiver_district VARCHAR(2),
    receiver_neighborhood VARCHAR(2),
    receiver_other_signs TEXT,
    receiver_phone_country VARCHAR(4),
    receiver_phone_number VARCHAR(20),
    receiver_email VARCHAR(255),
    summary_total_taxed_services NUMERIC(18,5),
    summary_total_exempt_services NUMERIC(18,5),
    summary_total_taxed_goods NUMERIC(18,5),
    summary_total_exempt_goods NUMERIC(18,5),
    summary_total_taxed NUMERIC(18,5),
    summary_total_exempt NUMERIC(18,5),
    summary_total_sale NUMERIC(18,5),
    summary_total_discounts NUMERIC(18,5),
    summary_total_net_sale NUMERIC(18,5),
    summary_total_tax NUMERIC(18,5),
    summary_total_iva_refund NUMERIC(18,5),
    summary_total_other_charges NUMERIC(18,5),
    summary_total_voucher NUMERIC(18,5),
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
    commercial_code VARCHAR(64) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity NUMERIC(18,5) NOT NULL,
    measurement_unit VARCHAR(16) NOT NULL,
    commercial_unit VARCHAR(16),
    unit_price NUMERIC(18,5) NOT NULL,
    total_amount NUMERIC(18,5) NOT NULL,
    discount_amount NUMERIC(18,5),
    discount_reason VARCHAR(255),
    subtotal NUMERIC(18,5) NOT NULL,
    taxable_base NUMERIC(18,5),
    total_line_amount NUMERIC(18,5) NOT NULL
);

CREATE TABLE invoice_taxes (
    id SERIAL PRIMARY KEY,
    invoice_item_id BIGINT NOT NULL REFERENCES invoice_items(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL REFERENCES tenants(id),
    tax_code VARCHAR(64) NOT NULL,
    rate_code VARCHAR(32),
    tax_rate NUMERIC(5,2) NOT NULL,
    factor_iva NUMERIC(6,5),
    taxable_base NUMERIC(18,5) NOT NULL,
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
