package com.facturador.invoice.util;

import com.facturador.tenant.model.Tenant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class ClaveGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");

    public String generateClave(Tenant tenant, String documentType, long sequence) {
        String date = LocalDate.now().format(DATE_FORMAT);
        String sequenceStr = String.format("%08d", sequence);
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "%s%s%s%04d".formatted(documentType, date, sequenceStr, random);
    }
}
