package com.roomify.detection_be.web.advice;

import com.roomify.detection_be.constants.WebSocketPath;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketErrorsHandler {
    private final SimpMessagingTemplate messagingTemplate;

    public WebsocketErrorsHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(WebSocketPath.ERRORS)
    public void handleErrors(String errorMessage) {
        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_ERRORS, errorMessage);
    }
}
