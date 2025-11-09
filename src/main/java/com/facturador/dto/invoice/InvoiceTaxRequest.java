package com.facturador.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record InvoiceTaxRequest(
    @NotBlank String taxCode,
    String rateCode,
    @NotNull @Positive BigDecimal taxRate,
    @PositiveOrZero BigDecimal factorIVA,
    @NotNull @PositiveOrZero BigDecimal taxableBase,
    @NotNull @PositiveOrZero BigDecimal taxAmount
) {
}
