package com.facturador.tenant.mapper;

import com.facturador.tenant.model.Tenant;
import com.facturador.tenant.dto.TenantResponse;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper {

    public TenantResponse toResponse(Tenant tenant) {
        return toResponse(tenant, tenant.getCertificateInline() != null);
    }

    public TenantResponse toResponse(Tenant tenant, boolean hasCertificate) {
        return new TenantResponse(
            tenant.getId(),
            tenant.getName(),
            tenant.getApiKey(),
            tenant.getEnvironment(),
            hasCertificate
        );
    }
}
