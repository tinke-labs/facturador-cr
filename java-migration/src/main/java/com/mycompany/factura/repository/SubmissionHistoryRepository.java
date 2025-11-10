package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.SubmissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionHistoryRepository extends JpaRepository<SubmissionHistory, Long> {
}
