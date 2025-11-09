package com.facturador.dto.invoice;

import jakarta.validation.constraints.NotBlank;

public record ReceiverTelephoneRequest(
    @NotBlank String countryCode,
    @NotBlank String number
) {
}
