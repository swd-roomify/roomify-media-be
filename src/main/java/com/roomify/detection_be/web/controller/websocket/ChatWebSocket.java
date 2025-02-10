package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.constants.WebSocketPath;
import com.roomify.detection_be.web.dtos.req.UserChatReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocket {
    private static final Logger log = LoggerFactory.getLogger(ConnectionWebSocket.class);
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocket(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(WebSocketPath.CHAT)
    public void chat(@Payload UserChatReq message) {
        log.info("Received message: {}, {}, {}", message.getUserId(), message.getUsername(), message.getMessage() );
        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_CHAT, message);
    }
}
