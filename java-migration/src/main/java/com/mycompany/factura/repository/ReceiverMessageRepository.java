package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.ReceiverMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiverMessageRepository extends JpaRepository<ReceiverMessage, Long> {
}
