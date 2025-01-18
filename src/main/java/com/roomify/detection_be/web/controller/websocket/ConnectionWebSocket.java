package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.entity.User;
import com.roomify.detection_be.web.service.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class ConnectionWebSocket {
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    private final ConnectionService connectionService;
    private final SimpMessagingTemplate messagingTemplate;

    public ConnectionWebSocket(ConnectionService connectionService,
                               SimpMessagingTemplate messagingTemplate) {
        this.connectionService = connectionService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(Path.JOIN)
    public void join(User message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String userId = connectionService.saveUserAndSession(message, sessionId);

        messagingTemplate.convertAndSend(Path.TOPIC_POSITION, connectionService.getAllUsers());

        log.info("User {} joined session {} with ID {}", message.getUsername(), sessionId, userId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        connectionService.removeUserBySession(sessionId);

        messagingTemplate.convertAndSend(Path.TOPIC_POSITION, connectionService.getAllUsers());

        log.info("User disconnected, session ID: {}", sessionId);
    }
}
