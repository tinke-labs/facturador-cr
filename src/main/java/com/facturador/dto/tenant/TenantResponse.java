package com.facturador.dto.tenant;

import com.facturador.models.tenant.Tenant.Environment;

public record TenantResponse(Long id, String name, String apiKey, Environment environment, boolean hasCertificate) {
}
