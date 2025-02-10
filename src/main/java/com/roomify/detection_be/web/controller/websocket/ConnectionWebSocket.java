package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.constants.WebSocketPath;
import com.roomify.detection_be.web.dtos.req.UserJoinReq;
import com.roomify.detection_be.web.service.websocket.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class ConnectionWebSocket {
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    private final ConnectionService connectionService;
    private final SimpMessagingTemplate messagingTemplate;

    public ConnectionWebSocket(ConnectionService connectionService,
                               SimpMessagingTemplate messagingTemplate) {
        this.connectionService = connectionService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(WebSocketPath.JOIN)
    public void join(UserJoinReq user, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Raw message: {}", user);
        log.info("Received user details: userId={}, username={}, character={}",
                user.getUserId(), user.getUsername(), user.getCharacter());

        String sessionId = headerAccessor.getSessionId();
        String userId = connectionService.saveUserAndSession(user, sessionId);

        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_POSITION, connectionService.getAllUsers());

        log.info("User {} joined session {} with ID {} and character {}", user.getUsername(), sessionId, userId, user.getCharacter());
    }

}
