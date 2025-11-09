package com.facturador.controllers;

import com.facturador.dto.tenant.CertificateRequest;
import com.facturador.dto.tenant.TenantResponse;
import com.facturador.services.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenant(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenant(id));
    }

    @PostMapping("/{id}/certificate")
    public ResponseEntity<TenantResponse> uploadCertificate(@PathVariable Long id,
                                                             @Valid @RequestBody CertificateRequest request) {
        TenantResponse response = tenantService.updateCertificate(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
