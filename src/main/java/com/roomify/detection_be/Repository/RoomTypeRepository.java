package com.roomify.detection_be.repository;

import com.roomify.detection_be.web.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
    boolean existsByName(String name);
    Optional<RoomType> findByName(String name);
}
