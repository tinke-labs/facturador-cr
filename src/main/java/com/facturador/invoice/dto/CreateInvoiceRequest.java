package com.facturador.invoice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateInvoiceRequest(
    @NotBlank String documentType,
    @NotBlank String currency,
    @Positive double total,
    @NotBlank String customerName,
    @NotEmpty List<@Valid InvoiceItemRequest> items,
    List<@Valid InvoiceTaxRequest> taxes
) {
}
