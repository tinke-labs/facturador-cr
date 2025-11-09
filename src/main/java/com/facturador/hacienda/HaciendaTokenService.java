package com.facturador.hacienda;

import com.facturador.tenant.model.Tenant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class HaciendaTokenService {

    private final HaciendaClient haciendaClient;

    public HaciendaTokenService(HaciendaClient haciendaClient) {
        this.haciendaClient = haciendaClient;
    }

    @Cacheable(cacheNames = "haciendaTokens", key = "#tenant.id")
    public String getToken(Tenant tenant) {
        return haciendaClient.obtainToken(tenant);
    }

    @CacheEvict(cacheNames = "haciendaTokens", key = "#tenantId")
    public void evictToken(Long tenantId) {
        // Cache eviction handled by annotation.
    }
}
