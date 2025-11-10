package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.EnvioHistorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioHistorialRepository extends JpaRepository<EnvioHistorial, Long> {
}
