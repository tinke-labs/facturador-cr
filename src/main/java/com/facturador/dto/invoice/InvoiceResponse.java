package com.facturador.dto.invoice;

import java.time.OffsetDateTime;

public record InvoiceResponse(
    String clave,
    String status,
    String location,
    OffsetDateTime createdAt
) {
}
