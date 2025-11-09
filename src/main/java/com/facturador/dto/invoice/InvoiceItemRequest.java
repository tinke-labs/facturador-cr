package com.facturador.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record InvoiceItemRequest(
    @Positive int lineNumber,
    @NotBlank String description,
    @Positive double quantity,
    @Positive double unitPrice,
    @Positive double subtotal,
    double taxAmount
) {
}
