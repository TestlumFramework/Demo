package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.KafkaApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${kafka.enabled:true}")
public class KafkaController implements KafkaApi {

    @Autowired
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    @Autowired
    private final KafkaConsumer<Integer, String> kafkaConsumer;
    @Autowired
    private final NewTopic defaultTopicOne;

    @SneakyThrows
    public ResponseEntity<String> sendKafkaMessageToDefaultTopic(String message) {
        log.info(message);
        ListenableFuture<SendResult<Integer, String>> sendResult = kafkaTemplate.send(defaultTopicOne.name(), message);
        kafkaTemplate.flush();
        return (nonNull(sendResult.get()))
                ? ResponseEntity.ok(message)
                : ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<String>> receiveKafkaMessagesFromDefaultTopic() {
        List<String> messages = getKafkaMessages(defaultTopicOne.name());
        return ResponseEntity.ok(messages);
    }

    @SneakyThrows
    public ResponseEntity<String> sendKafkaMessage(String message, String topic) {
        log.info(message);
        NewTopic newTopic = TopicBuilder.name(topic).build();
        ListenableFuture<SendResult<Integer, String>> sendResult = kafkaTemplate.send(newTopic.name(), message);
        kafkaTemplate.flush();
        return (nonNull(sendResult.get()))
                ? ResponseEntity.ok(message)
                : ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<String>> receiveKafkaMessages(String topic) {
        List<String> messages = getKafkaMessages(topic);
        return ResponseEntity.ok(messages);
    }

    private List<String> getKafkaMessages(String topic) {
        List<String> topics = Collections.singletonList(topic);
        kafkaConsumer.subscribe(topics);
        ConsumerRecords<Integer, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));
        List<String> messages = new ArrayList<>();
        for (ConsumerRecord<Integer, String> record : records) {
            messages.add(record.value());
        }
        kafkaConsumer.commitSync();
        return messages;
    }
}
