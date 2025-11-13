package com.facturador.dto.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record InvoiceSummaryRequest(
    @NotNull @PositiveOrZero BigDecimal totalTaxedServices,
    @NotNull @PositiveOrZero BigDecimal totalExemptServices,
    @NotNull @PositiveOrZero BigDecimal totalTaxedGoods,
    @NotNull @PositiveOrZero BigDecimal totalExemptGoods,
    @NotNull @PositiveOrZero BigDecimal totalTaxed,
    @NotNull @PositiveOrZero BigDecimal totalExempt,
    @NotNull @PositiveOrZero BigDecimal totalSale,
    @NotNull @PositiveOrZero BigDecimal totalDiscounts,
    @NotNull @PositiveOrZero BigDecimal totalNetSale,
    @NotNull @PositiveOrZero BigDecimal totalTax,
    @NotNull @PositiveOrZero BigDecimal totalIVARefund,
    @NotNull @PositiveOrZero BigDecimal totalOtherCharges,
    @NotNull @PositiveOrZero BigDecimal totalVoucher
) {
}
