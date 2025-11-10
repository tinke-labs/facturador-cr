package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.EstadoHacienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoHaciendaRepository extends JpaRepository<EstadoHacienda, Long> {
    Optional<EstadoHacienda> findByComprobanteClave(String clave);
}
