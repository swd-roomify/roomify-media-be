package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.constants.WebSocketPath;
import com.roomify.detection_be.web.entity.User;
import com.roomify.detection_be.web.service.MoveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MoveWebSocket {
    private static final Logger log = LoggerFactory.getLogger(MoveWebSocket.class);
    private final MoveService moveService;
    private final SimpMessagingTemplate messagingTemplate;

    public MoveWebSocket(MoveService moveService, SimpMessagingTemplate messagingTemplate) {
        this.moveService = moveService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(WebSocketPath.PATH)
    public void move(@Payload User message) {
        User user = User.builder()
                .username(message.getUsername())
                .positionX(message.getPositionX())
                .positionY(message.getPositionY())
                .build();

        moveService.saveUserPosition(user);

        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_POSITION, moveService.getAllUsers());
    }
}
