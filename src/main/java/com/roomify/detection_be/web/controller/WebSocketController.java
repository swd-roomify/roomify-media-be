package com.roomify.detection_be.web.controller;

import com.roomify.detection_be.thread.PositionUpdateQueue;
import com.roomify.detection_be.web.entity.Position;
import com.roomify.detection_be.web.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
  private final PositionUpdateQueue positionUpdateQueue;

  public WebSocketController(
      SimpMessagingTemplate messagingTemplate, PositionUpdateQueue positionUpdateQueue) {
    this.messagingTemplate = messagingTemplate;
    this.positionUpdateQueue = positionUpdateQueue;
  }

  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Moving"),
        @ApiResponse(responseCode = "200", description = "Internal server error")
      })
  @MessageMapping("/move")
  public void move(User message) {
    positionUpdateQueue.addUpdate(
        message.getUsername(), new Position(message.getPositionX(), message.getPositionY()));

    users.put(
        message.getUsername(),
        User.builder()
            .username(message.getUsername())
            .positionX(message.getPositionX())
            .positionY(message.getPositionY())
            .build());
  }

  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully connected to WebSocket"),
        @ApiResponse(responseCode = "200", description = "Internal server error")
      })
  @Operation(
      summary = "Establish WebSocket connection",
      description =
          "This endpoint is used to establish a WebSocket connection. It does not perform an HTTP request but initiates a WebSocket connection.")
  @MessageMapping("/join")
  public void join(User message, SimpMessageHeaderAccessor headerAccessor) {
    String username = message.getUsername();

    users.putIfAbsent(
        username, User.builder().username(username).positionX(0).positionY(0).build());

    String sessionId = headerAccessor.getSessionId();
    sessionToUsername.put(sessionId, username);

    messagingTemplate.convertAndSend("/topic/positions", users);
  }

  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    String username = sessionToUsername.remove(sessionId);
    if (username != null) {
      users.remove(username);

      messagingTemplate.convertAndSend("/topic/positions", users);
    }
  }
}
