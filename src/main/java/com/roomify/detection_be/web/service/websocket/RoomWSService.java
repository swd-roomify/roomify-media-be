package com.roomify.detection_be.web.service.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoomWSService {
    @Autowired
    private HashOperations<String, String, String> roomSessionHashOperations;

    public void addSessionToRoom(String roomId, String sessionId, String userId) {
        roomSessionHashOperations.put(roomId, sessionId, userId);
    }

    public void removeSessionFromRoom(String roomId, String sessionId) {
        roomSessionHashOperations.delete(roomId, sessionId);
    }

    public Map<String, String> getSessionsInRoom(String roomId) {
        return roomSessionHashOperations.entries(roomId);
    }

    public void clearRoomSessions(String roomId) {
        roomSessionHashOperations.getOperations().delete(roomId);
    }
}
