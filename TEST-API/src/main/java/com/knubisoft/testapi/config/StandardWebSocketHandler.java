package com.knubisoft.testapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class StandardWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        log.info("Standard websocket connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session,
                                     final TextMessage message) throws Exception {
        final String payload = message.getPayload();
        log.info("Standard websocket received: {}", payload);
        session.sendMessage(new TextMessage(payload));
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session,
                                      final CloseStatus status) {
        log.info("Standard websocket closed: {}", session.getId());
    }
}