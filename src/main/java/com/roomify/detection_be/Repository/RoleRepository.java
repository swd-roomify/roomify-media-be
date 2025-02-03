package com.roomify.detection_be.Repository;

import com.roomify.detection_be.web.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
   Optional<Role> findByName(String name);
}
