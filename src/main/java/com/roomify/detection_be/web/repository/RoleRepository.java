package com.roomify.detection_be.web.repository;

import com.roomify.detection_be.web.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByName(String name);

  boolean existsByName(String name);
}
