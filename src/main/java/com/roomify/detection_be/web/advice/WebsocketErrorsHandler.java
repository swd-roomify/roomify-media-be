package com.roomify.detection_be.web.advice;

import com.roomify.detection_be.web.controller.Path;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketErrorsHandler {
  private final SimpMessagingTemplate messagingTemplate;

  public WebsocketErrorsHandler(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @MessageMapping(Path.ERRORS)
  public void handleErrors(String errorMessage) {
    messagingTemplate.convertAndSend(Path.TOPIC_ERRORS, errorMessage);
  }
}
