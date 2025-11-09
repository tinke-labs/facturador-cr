package com.facturador.utils;

import com.facturador.models.tenant.Tenant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class ClaveGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyy");
    private static final String COUNTRY_CODE = "506";

    public String generateClave(Tenant tenant, String documentType, long sequence, OffsetDateTime issueDate) {
        String date = issueDate.format(DATE_FORMAT);
        String identification = leftPad(tenant.getIdentificationNumber(), 12, '0');
        String situation = tenant.getSituation();
        String consecutive = generateConsecutive(tenant, documentType, sequence);
        String securityCode = String.format("%08d", ThreadLocalRandom.current().nextInt(0, 100_000_000));
        return COUNTRY_CODE + date + identification + situation + consecutive + securityCode;
    }

    public String generateConsecutive(Tenant tenant, String documentType, long sequence) {
        String branch = leftPad(tenant.getBranchCode(), 3, '0');
        String terminal = leftPad(tenant.getTerminalCode(), 5, '0');
        String docType = leftPad(documentType, 2, '0');
        String sequential = String.format("%010d", sequence);
        return branch + terminal + docType + sequential;
    }

    private String leftPad(String value, int length, char fill) {
        if (value == null) {
            throw new IllegalStateException("Missing value required for clave generation");
        }
        if (value.length() >= length) {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = value.length(); i < length; i++) {
            builder.append(fill);
        }
        builder.append(value);
        return builder.toString();
    }
}
