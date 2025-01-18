package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.constants.WebSocketPath;
import com.roomify.detection_be.web.entity.req.UserJoinReq;
import com.roomify.detection_be.web.entity.res.UserGenerateRes;
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

    @MessageMapping(WebSocketPath.JOIN)
    public void join(UserJoinReq user, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Raw message: {}", user);
        log.info("Received user details: userId={}, username={}",
                user.getUserId(), user.getUsername());

        String sessionId = headerAccessor.getSessionId();
        String userId = connectionService.saveUserAndSession(user, sessionId);

        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_POSITION, connectionService.getAllUsers());

        log.info("User {} joined session {} with ID {}", user.getUsername(), sessionId, userId);
    }


    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        connectionService.removeUserBySession(sessionId);

        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_POSITION, connectionService.getAllUsers());

        log.info("User disconnected, session ID: {}", sessionId);
    }
}
