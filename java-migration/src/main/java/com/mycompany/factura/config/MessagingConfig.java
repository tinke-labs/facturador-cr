package com.mycompany.factura.config;

import com.mycompany.factura.config.properties.QueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
@EnableRabbit
public class MessagingConfig {

    private final QueueProperties queueProperties;

    public MessagingConfig(QueueProperties queueProperties) {
        this.queueProperties = queueProperties;
    }

    @Bean
    public Queue retryQueue() {
        return new Queue(queueProperties.getRetryQueue(), true, false, false, MapBuilder.deadLetter(queueProperties.getDeadLetterQueue()));
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(queueProperties.getDeadLetterQueue(), true);
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(queueProperties.getRetryExchange(), true, false);
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryQueue()).to(retryExchange()).with(queueProperties.getRetryRouting());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setChannelTransacted(true);
        return template;
    }

    private static class MapBuilder {
        static java.util.Map<String, Object> deadLetter(String deadLetterQueue) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("x-dead-letter-exchange", "");
            map.put("x-dead-letter-routing-key", deadLetterQueue);
            return map;
        }
    }
}
