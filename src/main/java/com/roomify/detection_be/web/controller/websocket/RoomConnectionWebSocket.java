package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.constants.WebSocketPath;
import com.roomify.detection_be.web.dtos.req.UserJoinReq;
import com.roomify.detection_be.web.service.websocket.RoomWSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Controller
public class RoomConnectionWebSocket {
    @Autowired
    private RoomWSService roomSessionService;
    private static final Logger log = LoggerFactory.getLogger(RoomConnectionWebSocket.class);
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//
//        String sessionId = headerAccessor.getSessionId();
//        String roomId = headerAccessor.getFirstNativeHeader("roomId");
//        String userId = headerAccessor.getFirstNativeHeader("userId");
//        String username = headerAccessor.getFirstNativeHeader("username");
//        String character = headerAccessor.getFirstNativeHeader("character");
//
//        UserJoinReq user = UserJoinReq.builder().userId(userId).username(username).character(character).build();
//        log.info("Raw message: {}", user);
//        log.info("Received user details: userId={}, username={}, character={}",
//                user.getUserId(), user.getUsername(), user.getCharacter());
//        roomSessionService.addSessionToRoom(roomId, sessionId, user);
//    }

    @MessageMapping(WebSocketPath.JOIN)
    public void join(@Header String roomId , UserJoinReq user, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Raw message: {}", user);
        log.info("Received user details: userId={}, username={}, character={}",
                user.getUserId(), user.getUsername(), user.getCharacter());
        String sessionId = headerAccessor.getSessionId();
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("roomId", roomId);
        roomSessionService.addSessionToRoom(roomId, sessionId, user);
        log.info("User {} joined session {} with ID {} and character {}", user.getUsername(), sessionId, user.getUserId(), user.getCharacter());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String roomId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("roomId");
        log.info("Session {} left the room {}", sessionId, roomId);
        roomSessionService.removeSessionFromRoom(roomId,sessionId);
    }
}
