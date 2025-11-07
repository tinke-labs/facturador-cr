package com.facturador.invoice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record InvoiceTaxRequest(
    @NotBlank String taxType,
    @PositiveOrZero double taxRate,
    @PositiveOrZero double taxAmount
) {
}
