package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.WebSocketApi;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@ConditionalOnExpression("${spring.websocket.enabled:true}")
public class WebSocketController implements WebSocketApi {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @SneakyThrows
    public void somePing(final String arg) {
        messagingTemplate.convertAndSend("/topic/ping", new Ping(arg));
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @SneakyThrows
    public void someTest(final String arg) {
        messagingTemplate.convertAndSend("/topic/test", "some test message");
        TimeUnit.MILLISECONDS.sleep(1000);
        messagingTemplate.convertAndSend("/topic/test", arg);
    }

    @Scheduled(fixedDelay = 1000)
    public void somePeriodicMessages() {
        messagingTemplate.convertAndSend("/topic/server", "server periodic message");
    }

    @Data
    private static class Ping {
        private final String value;
    }
}
