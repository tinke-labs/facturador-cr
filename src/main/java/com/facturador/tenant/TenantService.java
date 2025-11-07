package com.facturador.tenant;

import com.facturador.tenant.Tenant.Environment;
import com.facturador.tenant.dto.CertificateRequest;
import com.facturador.tenant.dto.TenantResponse;
import com.facturador.tenant.exception.TenantNotFoundException;
import com.facturador.tenant.exception.TenantValidationException;
import com.facturador.hacienda.HaciendaTokenService;
import com.facturador.storage.EncryptionService;
import com.facturador.tenant.mapper.TenantMapper;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final EncryptionService encryptionService;
    private final HaciendaTokenService haciendaTokenService;

    public TenantService(TenantRepository tenantRepository,
                         TenantMapper tenantMapper,
                         EncryptionService encryptionService,
                         HaciendaTokenService haciendaTokenService) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
        this.encryptionService = encryptionService;
        this.haciendaTokenService = haciendaTokenService;
    }

    public Tenant resolveByApiKey(String apiKey) {
        return tenantRepository.findByApiKey(apiKey)
            .orElseThrow(() -> new TenantNotFoundException("Invalid API key"));
    }

    public TenantResponse getTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
            .orElseThrow(() -> new TenantNotFoundException("Tenant not found"));
        return tenantMapper.toResponse(tenant);
    }

    @Transactional
    public TenantResponse updateCertificate(Long id, CertificateRequest request) {
        Tenant tenant = tenantRepository.findById(id)
            .orElseThrow(() -> new TenantNotFoundException("Tenant not found"));

        tenant.setCertificateInline(request.certificate());
        tenant.setCertificatePinEncrypted(encryptionService.encrypt(request.pin()));
        tenant.setUpdatedAt(OffsetDateTime.now());
        try {
            tenant.setEnvironment(Environment.valueOf(request.environment().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new TenantValidationException("Invalid environment. Use SANDBOX or PRODUCTION");
        }
        tenantRepository.save(tenant);
        haciendaTokenService.evictToken(id);

        return tenantMapper.toResponse(tenant, true);
    }

    @Transactional
    public void registerTenant(String name, String apiKey, Environment environment) {
        Optional<Tenant> existing = tenantRepository.findByApiKey(apiKey);
        if (existing.isPresent()) {
            throw new TenantValidationException("API key already exists");
        }
        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenant.setApiKey(apiKey);
        tenant.setEnvironment(environment);
        tenantRepository.save(tenant);
    }
}
