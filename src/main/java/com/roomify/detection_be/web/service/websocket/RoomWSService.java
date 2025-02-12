package com.roomify.detection_be.web.service.websocket;

import com.roomify.detection_be.web.dtos.req.UserChatReq;
import com.roomify.detection_be.web.dtos.req.UserJoinReq;
import com.roomify.detection_be.web.dtos.req.UserMoveReq;
import com.roomify.detection_be.web.dtos.res.UserChatWSRes;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomWSService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private HashOperations<String, String, String> roomSessionHashOperations;
    @Autowired
    private HashOperations<String, String, UserWSRes> roomUserHashOperations;
    public void addSessionToRoom(String roomId, String sessionId, UserJoinReq user) {
        UserWSRes savedUser = new UserWSRes(user.getUserId(), user.getUsername(), user.getCharacter(), 400, 400);
        roomSessionHashOperations.put(roomId, sessionId, user.getUserId());
        roomUserHashOperations.put(roomId, user.getUserId(), savedUser);
        broadcastToUserByRoomId("/join", roomId);
    }

    public void removeSessionFromRoom(String roomId, String sessionId) {
        if (roomSessionHashOperations.hasKey(roomId, sessionId)) {
            roomSessionHashOperations.delete(roomId, sessionId);
        }
        if (roomUserHashOperations.hasKey(roomId, sessionId)) {
            roomUserHashOperations.delete(roomId, sessionId);
        }
        if (roomSessionHashOperations.entries(roomId).isEmpty()) {
            clearRoomSessions(roomId);
        } else {
            broadcastToUserByRoomId("/leave", roomId);
        }
    }
    public void broadcastToUserByRoomId(String destination, String roomId) {
        messagingTemplate.convertAndSend(
                destination + "/" + roomId,
                getUsersByRoomId(roomId)
        );
    }
//    public Map<String, String> getSessionsInRoom(String roomId) {
//        return roomSessionHashOperations.entries(roomId);
//    }

    public void clearRoomSessions(String roomId) {
        if (roomSessionHashOperations.getOperations().hasKey(roomId)) {
            roomSessionHashOperations.getOperations().delete(roomId);
        }
        if (roomUserHashOperations.getOperations().hasKey(roomId)) {
            roomUserHashOperations.getOperations().delete(roomId);
        }
    }

    public List<UserWSRes> getUsersByRoomId(String roomId) {
        Map<String, UserWSRes> usersMap = roomUserHashOperations.entries(roomId);
        return new ArrayList<>(usersMap.values());
    }

    public void handleUserMove(String roomId, UserMoveReq userMoveReq) {
        if (roomUserHashOperations.hasKey(roomId, userMoveReq.getUserId())) {
            UserWSRes user = roomUserHashOperations.get(roomId, userMoveReq.getUserId());
            user.setPositionX(userMoveReq.getPositionX());
            user.setPositionY(userMoveReq.getPositionY());
            roomUserHashOperations.put(roomId, userMoveReq.getUserId(), user);
            broadcastToUserByRoomId("/move", roomId);
        }
    }

    public void handleUserChat(String roomId, UserChatReq user) {
        if (roomUserHashOperations.hasKey(roomId, user.getUserId())) {
            UserWSRes foundUser = roomUserHashOperations.get(roomId, user.getUserId());
            messagingTemplate.convertAndSend("/chat/" + roomId,
                    new UserChatWSRes(user.getUserId(),
                            foundUser.getUsername(),
                            user.getMessage()));
        }
    }
}
