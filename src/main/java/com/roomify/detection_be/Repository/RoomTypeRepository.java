package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entities.RoomType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
  boolean existsByName(String name);

  Optional<RoomType> findByName(String name);
}
