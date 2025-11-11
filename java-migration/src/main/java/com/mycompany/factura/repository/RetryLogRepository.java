package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.RetryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetryLogRepository extends JpaRepository<RetryLog, Long> {
    List<RetryLog> findByDocumentKeyOrderByAttemptedAtDesc(String key);
}
