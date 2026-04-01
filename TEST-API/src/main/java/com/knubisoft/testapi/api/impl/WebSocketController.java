package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.WebSocketApi;
import com.knubisoft.testapi.dto.websocket.stomp.StompSelfMessage;
import com.knubisoft.testapi.dto.websocket.stomp.StompSelfResponse;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@ConditionalOnExpression("${spring.websocket.enabled:true}")
public class WebSocketController implements WebSocketApi {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final Map<String, String> selfStorage = new ConcurrentHashMap<>();

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

    @Override
    public void createSelf(final StompSelfMessage message) {
        selfStorage.put(message.getId(), message.getValue());
        messagingTemplate.convertAndSend(
                "/topic/self/create",
                new StompSelfResponse("CREATED", message.getId(), message.getValue())
        );
    }

    @Override
    public void getSelf(final StompSelfMessage message) {
        final String value = selfStorage.get(message.getId());
        if (value == null) {
            messagingTemplate.convertAndSend(
                    "/topic/self/get",
                    new StompSelfResponse("NOT_FOUND", message.getId(), null)
            );
            return;
        }
        messagingTemplate.convertAndSend(
                "/topic/self/get",
                new StompSelfResponse("FOUND", message.getId(), value)
        );
    }

    @Override
    public void deleteSelf(final StompSelfMessage message) {
        selfStorage.remove(message.getId());
        messagingTemplate.convertAndSend(
                "/topic/self/delete",
                new StompSelfResponse("DELETED", message.getId(), null)
        );
    }

    @Override
    public void resetSelf() {
        selfStorage.clear();
        messagingTemplate.convertAndSend(
                "/topic/self/reset",
                new StompSelfResponse("RESET", null, null)
        );
    }

    @Data
    private static class Ping {
        private final String value;
    }
}