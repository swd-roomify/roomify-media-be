package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.service.websocket.RoomWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private RoomWSService roomSessionService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String roomId = headerAccessor.getFirstNativeHeader("roomId");
        String userId = headerAccessor.getFirstNativeHeader("userId");

        if (roomId != null && userId != null) {
            roomSessionService.addSessionToRoom(roomId, sessionId, userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        for (String roomId : roomSessionService.getSessionsInRoom(headerAccessor.getFirstNativeHeader("roomId")).keySet()) {
            roomSessionService.removeSessionFromRoom(roomId, sessionId);
        }
    }
}
