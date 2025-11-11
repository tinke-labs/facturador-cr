package com.mycompany.factura.utils.queue;

import com.mycompany.factura.config.properties.QueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RetryPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final QueueProperties queueProperties;

    public RetryPublisher(RabbitTemplate rabbitTemplate, QueueProperties queueProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueProperties = queueProperties;
    }

    public void publishRetry(String key, String reason) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("clave", key);
        payload.put("motivo", reason);
        rabbitTemplate.convertAndSend(queueProperties.getRetryExchange(), queueProperties.getRetryRouting(), payload);
    }
}
