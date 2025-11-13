package com.facturador.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class LocalDocumentStorageService implements DocumentStorageService {

    private static final String BASE_DIRECTORY = "storage";

    @Override
    public String storeSignedXml(Long tenantId, String clave, String signedXml) {
        try {
            Path directory = Path.of(BASE_DIRECTORY, String.valueOf(tenantId));
            Files.createDirectories(directory);
            Path target = directory.resolve(clave + ".xml");
            Files.writeString(target, signedXml, StandardCharsets.UTF_8);
            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to persist signed XML", e);
        }
    }
}
