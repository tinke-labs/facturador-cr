package com.facturador.dto.invoice;

import jakarta.validation.constraints.NotBlank;

public record ReceiverLocationRequest(
    @NotBlank String province,
    @NotBlank String canton,
    @NotBlank String district,
    String neighborhood,
    @NotBlank String otherSigns
) {
}
