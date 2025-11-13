package com.facturador.storage;

public interface DocumentStorageService {

    String storeSignedXml(Long tenantId, String clave, String signedXml);
}
