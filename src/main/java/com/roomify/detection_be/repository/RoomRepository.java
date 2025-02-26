package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entities.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
  Optional<Room> findByRoomCode(String roomCode);

  List<Room> findByHost_UserId(String userId);
}
