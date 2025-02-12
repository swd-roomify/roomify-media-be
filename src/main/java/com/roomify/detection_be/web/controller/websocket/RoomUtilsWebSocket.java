package com.roomify.detection_be.web.controller.websocket;

import com.roomify.detection_be.web.dtos.req.UserChatReq;
import com.roomify.detection_be.web.dtos.req.UserMoveReq;
import com.roomify.detection_be.web.service.websocket.RoomWSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RoomUtilsWebSocket {

    @Autowired
    private RoomWSService roomSessionService;
    private static final Logger log = LoggerFactory.getLogger(RoomUtilsWebSocket.class);

    @MessageMapping("/move/{roomId}")
    public void move(@DestinationVariable String roomId, UserMoveReq user) {
        roomSessionService.handleUserMove(roomId, user);
    }

    @MessageMapping("/chat/{roomId}")
    public void chat(@DestinationVariable String roomId, UserChatReq user) {
        log.info("Chat in room: {}", roomId);
        roomSessionService.handleUserChat(roomId, user);
    }
}
