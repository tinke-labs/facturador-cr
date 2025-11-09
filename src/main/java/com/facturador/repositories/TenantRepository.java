package com.facturador.repositories;

import com.facturador.models.tenant.Tenant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByApiKey(String apiKey);
}
