package com.mycompany.factura.repository;

import com.mycompany.factura.models.entities.ReintentoLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReintentoLogRepository extends JpaRepository<ReintentoLog, Long> {
    List<ReintentoLog> findByComprobanteClaveOrderByFechaIntentoDesc(String clave);
}
