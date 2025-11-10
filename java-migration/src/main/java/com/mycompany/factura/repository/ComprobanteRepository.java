package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
    Optional<Comprobante> findByClave(String clave);
}
