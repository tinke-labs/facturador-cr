package com.facturador.tenant.exception;

public class TenantValidationException extends RuntimeException {
    public TenantValidationException(String message) {
        super(message);
    }
}
