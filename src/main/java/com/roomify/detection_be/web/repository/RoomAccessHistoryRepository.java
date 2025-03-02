package com.roomify.detection_be.web.repository;

import com.roomify.detection_be.web.entities.RoomAccessHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAccessHistoryRepository extends JpaRepository<RoomAccessHistory, Integer> {
  List<RoomAccessHistory> findByRoomId(String roomId);
}
