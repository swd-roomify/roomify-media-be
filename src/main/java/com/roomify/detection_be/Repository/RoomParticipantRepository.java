package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.RoomParticipant;
import com.roomify.detection_be.web.entity.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, String> {
    void deleteByRoomIdAndUserUserId(String roomId, String userId);

    List<RoomParticipant> findUsersByRoomId(String roomId);
}
