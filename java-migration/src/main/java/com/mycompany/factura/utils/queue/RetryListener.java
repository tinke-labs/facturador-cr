package com.mycompany.factura.utils.queue;

import com.mycompany.factura.service.ComprobanteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RetryListener {

    private static final Logger log = LoggerFactory.getLogger(RetryListener.class);

    private final ComprobanteService comprobanteService;

    public RetryListener(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    @RabbitListener(queues = "#{queueProperties.retryQueue}")
    public void procesarReintento(Map<String, Object> payload) {
        String clave = (String) payload.get("clave");
        log.info("Procesando reintento para clave {}", clave);
        comprobanteService.consultarEstado(clave);
    }
}
