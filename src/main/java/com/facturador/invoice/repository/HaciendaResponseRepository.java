package com.facturador.invoice.repository;

import com.facturador.invoice.model.HaciendaResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HaciendaResponseRepository extends JpaRepository<HaciendaResponse, Long> {
    List<HaciendaResponse> findByInvoiceId(Long invoiceId);
}
