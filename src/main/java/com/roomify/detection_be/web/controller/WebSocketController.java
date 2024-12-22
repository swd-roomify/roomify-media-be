package com.roomify.detection_be.web.controller;

import com.roomify.detection_be.web.entity.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
public class WebSocketController {
  private final Map<String, User> users = new ConcurrentHashMap<>();
  private final Map<String, String> sessionToUsername = new ConcurrentHashMap<>();
  private final SimpMessagingTemplate messagingTemplate;

  public WebSocketController(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @MessageMapping("/move")
  @SendTo("/topic/map")
  public Map<String, User> move(User message) {
    String username = message.getUsername();
    users.put(
        username,
        User.builder()
            .username(username)
            .positionX(message.getPositionX())
            .positionY(message.getPositionY())
            .build());
    for (Map.Entry<String, String> entry : sessionToUsername.entrySet()) {
      System.out.println(entry.getKey() + " " + entry.getValue());
    }
    return users;
  }

  @MessageMapping("/join")
  @SendTo("/topic/positions")
  public Map<String, User> join(User message, SimpMessageHeaderAccessor headerAccessor) {
    String username = message.getUsername();
    users.putIfAbsent(
        username, User.builder().username(username).positionX(0).positionY(0).build());

    String sessionId = headerAccessor.getSessionId();
    sessionToUsername.put(sessionId, username);

    return users;
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    String username = sessionToUsername.remove(sessionId);
    if (username != null) {
      users.remove(username);
      messagingTemplate.convertAndSend("/topic/map", users);
    }
  }
}
