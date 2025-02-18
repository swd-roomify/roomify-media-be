package com.roomify.detection_be.repository;

import com.roomify.detection_be.web.entities.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, String> {
    void deleteByRoomIdAndUserUserId(String roomId, String userId);

    List<RoomParticipant> findUsersByRoomId(String roomId);
}
