package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.ElectronicDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<ElectronicDocument, Long> {
    Optional<ElectronicDocument> findByKey(String key);
}
