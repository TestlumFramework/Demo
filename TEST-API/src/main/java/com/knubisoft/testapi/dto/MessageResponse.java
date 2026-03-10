package com.knubisoft.testapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MessageResponse {

    private String messageId;
    private String receiptHandle;
    private String md5OfBody;
    private Map<MessageSystemAttributeName, String> attributes;
    private String body;
    private String md5OfMessageAttributes;
    private Map<String, MessageAttributeValue> messageAttributes;

}