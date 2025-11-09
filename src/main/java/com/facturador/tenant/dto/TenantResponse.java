package com.facturador.tenant.dto;

import com.facturador.tenant.model.Tenant.Environment;

public record TenantResponse(Long id, String name, String apiKey, Environment environment, boolean hasCertificate) {
}
