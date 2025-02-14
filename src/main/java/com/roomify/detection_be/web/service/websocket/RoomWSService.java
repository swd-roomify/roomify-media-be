package com.roomify.detection_be.web.service.websocket;

import com.roomify.detection_be.web.controller.websocket.RoomUtilsWebSocket;
import com.roomify.detection_be.web.dtos.req.UserChatReq;
import com.roomify.detection_be.web.dtos.req.UserJoinReq;
import com.roomify.detection_be.web.dtos.req.UserMoveReq;
import com.roomify.detection_be.web.dtos.res.UserChatWSRes;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.roomify.detection_be.web.constants.RedisKeyPrefix.*;

@Service
public class RoomWSService {

    private static final Logger log = LoggerFactory.getLogger(RoomUtilsWebSocket.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private HashOperations<String, String, UserWSRes> roomUserHashOperations;

    private String getRoomKey(String roomId) {
        return ROOM_KEY_PREFIX + roomId;
    }

    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }
    private String getUserKey(String userId) {
        return USER_KEY_PREFIX + userId;
    }
    public void addSessionToRoom(String roomId, String sessionId, UserJoinReq user) {
        UserWSRes savedUser = new UserWSRes(user.getUserId(), user.getUsername(), user.getCharacter(), 400, 400);
        roomUserHashOperations.put(getRoomKey(roomId), getSessionKey(sessionId), savedUser);
        broadcastToUserByRoomId("/join", roomId);
    }

    public void removeSessionFromRoom(String roomId, String sessionId) {

        if (roomUserHashOperations.hasKey(getRoomKey(roomId), getSessionKey(sessionId))) {
            roomUserHashOperations.delete(getRoomKey(roomId), getSessionKey(sessionId));
        }
        broadcastToUserByRoomId("/leave", roomId);
    }
    public void broadcastToUserByRoomId(String destination, String roomId) {
        messagingTemplate.convertAndSend(
                "/topic" + destination + "/" + roomId,
                getUsersByRoomId(roomId)
        );
    }
//    public Map<String, String> getSessionsInRoom(String roomId) {
//        return roomSessionHashOperations.entries(roomId);
//    }

    public void clearRoomSessions(String roomId) {
        if (roomUserHashOperations.getOperations().hasKey(getRoomKey(roomId))) {
            roomUserHashOperations.getOperations().delete(getRoomKey(roomId));
        }
    }

    public List<UserWSRes> getUsersByRoomId(String roomId) {
        Map<String, UserWSRes> usersMap = roomUserHashOperations.entries(getRoomKey(roomId));
        return new ArrayList<>(usersMap.values());
    }

    public void handleUserMove(String roomId, UserMoveReq userMoveReq, String sessionId) {
        log.info("User is moving with info: {}, {}, {}", userMoveReq.getUserId(), String.valueOf(userMoveReq.getPositionX()), String.valueOf(userMoveReq.getPositionY()));
        if (roomUserHashOperations.hasKey(getRoomKey(roomId), getSessionKey(sessionId))) {
            UserWSRes user = roomUserHashOperations.get(getRoomKey(roomId), getSessionKey(sessionId));
            user.setPositionX(userMoveReq.getPositionX());
            user.setPositionY(userMoveReq.getPositionY());
            roomUserHashOperations.put(getRoomKey(roomId), getSessionKey(sessionId), user);
            broadcastToUserByRoomId("/move", roomId);
        }
    }

    public void handleUserChat(String roomId, UserChatReq user , String sessionId) {
        if (roomUserHashOperations.hasKey(getRoomKey(roomId), getUserKey(user.getUserId()))) {
            UserWSRes foundUser = roomUserHashOperations.get(getRoomKey(roomId), getSessionKey(sessionId));
            messagingTemplate.convertAndSend("/chat/" + roomId,
                    new UserChatWSRes(user.getUserId(),
                            foundUser.getUsername(),
                            user.getMessage()));
        }
    }
}
