package com.facturador.dto.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceiverRequest(
    @NotBlank String name,
    @NotBlank String identificationType,
    @NotBlank String identificationNumber,
    String commercialName,
    @NotNull @Valid ReceiverLocationRequest location,
    @Valid ReceiverTelephoneRequest phone,
    @Email String email
) {
}
