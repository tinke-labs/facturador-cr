package com.mycompany.factura.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "factura.colas")
public class QueueProperties {

    private String retryQueue;
    private String retryExchange;
    private String retryRouting;
    private String deadLetterQueue;

    public String getRetryQueue() {
        return retryQueue;
    }

    public void setRetryQueue(String retryQueue) {
        this.retryQueue = retryQueue;
    }

    public String getRetryExchange() {
        return retryExchange;
    }

    public void setRetryExchange(String retryExchange) {
        this.retryExchange = retryExchange;
    }

    public String getRetryRouting() {
        return retryRouting;
    }

    public void setRetryRouting(String retryRouting) {
        this.retryRouting = retryRouting;
    }

    public String getDeadLetterQueue() {
        return deadLetterQueue;
    }

    public void setDeadLetterQueue(String deadLetterQueue) {
        this.deadLetterQueue = deadLetterQueue;
    }
}
