package com.knubisoft.testapi.config;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("${rabbitmq.enabled:true}")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.queue.in}")
    private String inQueueName;

    @Value("${rabbitmq.queue.out}")
    private String outQueueName;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.in}")
    private String inRoutingKey;

    @Value("${rabbitmq.routing-key.out}")
    private String outRoutingKey;

    @Bean
    public AmqpAdmin amqpAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue inQueue() {
        return new Queue(inQueueName, true);
    }

    @Bean
    public Queue outQueue() {
        return new Queue(outQueueName, true);
    }

    @Bean
    public Exchange directExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding inBinding(final Exchange exchange) {
        return BindingBuilder.bind(inQueue()).to(exchange).with(inRoutingKey).noargs();
    }

    @Bean
    public Binding outBinding(final Exchange exchange) {
        return BindingBuilder.bind(outQueue()).to(exchange).with(outRoutingKey).noargs();
    }
}
