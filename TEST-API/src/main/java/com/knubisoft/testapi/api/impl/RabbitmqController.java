package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.RabbitmqApi;
import com.knubisoft.testapi.exception.QueueNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${rabbitmq.enabled:true}")
public class RabbitmqController implements RabbitmqApi {

    private final AmqpAdmin amqpAdmin;
    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.queue.in}")
    private String inQueueName;

    @Value("${rabbitmq.queue.out}")
    private String outQueueName;

    @Value("${rabbitmq.routing-key.in}")
    private String inRoutingKey;

    @Value("${rabbitmq.routing-key.out}")
    private String outRoutingKey;


    public ResponseEntity<String> sendMessageToQueue(@RequestBody String message, @PathVariable String queue) {
        String routingKey = getRoutingKey(queue);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<String> receiveMessageFromQueue(@PathVariable String queue) {
        checkIfQueueExists(queue);
        Message message = rabbitTemplate.receive(queue);
        String body = nonNull(message) ? new String(message.getBody()) : null;
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<List<String>> receiveAllMessagesFromQueue(@PathVariable String queue) {
        checkIfQueueExists(queue);
        List<String> messages = new ArrayList<>();
        int messageCount = amqpAdmin.getQueueInfo(queue).getMessageCount();
        while (messageCount > 0) {
            messages.add(new String(rabbitTemplate.receive(queue).getBody(), StandardCharsets.UTF_8));
            messageCount--;
        }
        return ResponseEntity.ok(messages);
    }

    private void checkIfQueueExists(final String queue) {
        if (isNull(amqpAdmin.getQueueInfo(queue))) {
            throw new QueueNotFoundException(queue);
        }
    }

    private String getRoutingKey(final String queue) {
        if (inQueueName.equals(queue)) {
            return inRoutingKey;
        } else if (outQueueName.equals(queue)) {
            return outRoutingKey;
        }
        throw new QueueNotFoundException(queue);
    }
}
