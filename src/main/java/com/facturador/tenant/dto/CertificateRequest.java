package com.facturador.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CertificateRequest(
    @NotBlank String certificate,
    @NotBlank String pin,
    @NotNull String environment
) {}
