package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.HaciendaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HaciendaStatusRepository extends JpaRepository<HaciendaStatus, Long> {
    Optional<HaciendaStatus> findByDocumentKey(String key);
}
