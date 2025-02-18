package com.roomify.detection_be.repository;

import com.roomify.detection_be.web.entities.RoomAccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomAccessHistoryRepository extends JpaRepository<RoomAccessHistory, Integer> {
    List<RoomAccessHistory> findByRoomId(String roomId);
}
