package com.mycompany.factura.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class UuidGenerator {

    private UuidGenerator() {
    }

    public static String generarClave() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return timestamp + uuid.substring(0, 20);
    }
}
