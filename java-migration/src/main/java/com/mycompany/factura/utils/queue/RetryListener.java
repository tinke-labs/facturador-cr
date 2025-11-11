package com.mycompany.factura.utils.queue;

import com.mycompany.factura.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RetryListener {

    private static final Logger log = LoggerFactory.getLogger(RetryListener.class);

    private final DocumentService documentService;

    public RetryListener(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RabbitListener(queues = "#{queueProperties.retryQueue}")
    public void processRetry(Map<String, Object> payload) {
        String key = (String) payload.get("clave");
        log.info("Processing retry for key {}", key);
        documentService.queryStatus(key);
    }
}
