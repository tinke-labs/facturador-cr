package com.facturador.invoice.repository;

import com.facturador.invoice.model.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByClaveAndTenantId(String clave, Long tenantId);

    List<Invoice> findByStatus(String status);
}
