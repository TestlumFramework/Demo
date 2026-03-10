package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.SqsApi;
import com.knubisoft.testapi.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${amazon.sqs.enabled:true}")
public class SqsController implements SqsApi {

    private final SqsClient amazonSQS;

    @Value("${amazon.sqs.queue.name}")
    private String queueName;

    public ResponseEntity<String> sendMessageToSqs(@RequestBody String message) {
        amazonSQS.sendMessage(builder -> builder.queueUrl(getQueueUrl(amazonSQS))
                .messageBody(message));
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<List<MessageResponse>> receiveMessageFromSqs() {
        ReceiveMessageResponse messageResult = amazonSQS.receiveMessage(builder -> builder.queueUrl(getQueueUrl(amazonSQS)));
        return ResponseEntity.ok(messageResult.messages()
                .stream()
                .map(message -> MessageResponse.builder()
                        .messageId(message.messageId())
                        .receiptHandle(message.receiptHandle())
                        .md5OfBody(message.md5OfBody())
                        .attributes(message.attributes())
                        .body(message.body())
                        .md5OfMessageAttributes(message.md5OfMessageAttributes())
                        .messageAttributes(message.messageAttributes())
                        .build())
                .collect(Collectors.toList()));
    }

    public ResponseEntity<String> sendMessageToSqsNewQueue(@RequestBody String message, @PathVariable String queue) {
        String queueUrl = createNewQueueIfNotExists(queue);
        amazonSQS.sendMessage(builder -> builder.queueUrl(queueUrl).messageBody(message));
        return ResponseEntity.ok(message);
    }

    public ResponseEntity<List<MessageResponse>> receiveMessageFromSqsNewQueue(@PathVariable String queue) {
        String queueUrl = createNewQueueIfNotExists(queue);
        ReceiveMessageResponse messageResult = amazonSQS.receiveMessage(builder -> builder.queueUrl(queueUrl));
        return ResponseEntity.ok(messageResult.messages()
                .stream()
                .map(message -> MessageResponse.builder()
                        .messageId(message.messageId())
                        .receiptHandle(message.receiptHandle())
                        .md5OfBody(message.md5OfBody())
                        .attributes(message.attributes())
                        .body(message.body())
                        .md5OfMessageAttributes(message.md5OfMessageAttributes())
                        .messageAttributes(message.messageAttributes())
                        .build())
                .collect(Collectors.toList()));
    }

    private String getQueueUrl(SqsClient amazonSQS) {
        return amazonSQS.getQueueUrl(builder -> builder.queueName(queueName)).queueUrl();
    }

    private String createNewQueueIfNotExists(String queueName) {
        try {
            return amazonSQS.getQueueUrl(builder -> builder.queueName(queueName)).queueUrl();
        } catch (QueueDoesNotExistException exception) {
            return amazonSQS.createQueue(builder -> builder.queueName(queueName)).queueUrl();
        }
    }
}
