package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.service.websocket.RoomWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class RoomWebSocket {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomWSService roomSessionService;

    @MessageMapping("/send/{roomId}")
    public void sendMessageToRoom(@org.springframework.web.bind.annotation.PathVariable String roomId, String message) {
        Map<String, String> sessions = roomSessionService.getSessionsInRoom(roomId);

        for (String sessionId : sessions.keySet()) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/messages", message);
        }
    }

    @MessageMapping("/join/{roomId}")
    public void userJoinRoom(@PathVariable String roomId) {

    }
}
