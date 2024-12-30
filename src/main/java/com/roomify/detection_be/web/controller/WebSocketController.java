package com.roomify.detection_be.web.controller;

import com.roomify.detection_be.web.entity.User;
import com.roomify.detection_be.web.entity.payload.UserMove;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class WebSocketController {
  private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
  private final Map<String, User> users = new ConcurrentHashMap<>();
  private final Map<String, String> sessionToUsername = new ConcurrentHashMap<>();
  private final SimpMessagingTemplate messagingTemplate;

  public WebSocketController(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @MessageMapping(Path.PATH)
  public void move(@Valid @Payload UserMove message, SimpMessageHeaderAccessor headerAccessor) {
    String sessionId = headerAccessor.getSessionId();

    users.computeIfPresent(
        sessionId,
        (key, value) -> {
          value.setPositionX(message.getPositionX());
          value.setPositionY(message.getPositionY());
          return value;
        });

    messagingTemplate.convertAndSend(Path.TOPIC_POSITION, users);
  }

  @MessageMapping(Path.JOIN)
  public void join(User message, SimpMessageHeaderAccessor headerAccessor) {
    String username = message.getUsername();
    String sessionId = headerAccessor.getSessionId();

    users.putIfAbsent(
        sessionId, User.builder().username(username).positionX(0).positionY(0).build());

    sessionToUsername.put(sessionId, username);

    messagingTemplate.convertAndSend(Path.TOPIC_POSITION, users);
    log.info("User {} joined {}", username, sessionId);
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    String username = sessionToUsername.remove(sessionId);
    users.remove(sessionId);
    messagingTemplate.convertAndSend(Path.TOPIC_POSITION, users);

    log.info("User {} disconnected", username);
  }
}
