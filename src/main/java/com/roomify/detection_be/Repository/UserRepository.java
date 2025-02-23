package com.roomify.detection_be.repository;

import com.roomify.detection_be.web.entities.Users.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByGithubUserGithubUsername(String name);

    Optional<User> findByEmailAndProvidedId(String name, String provideId);

    Optional<User> findByProvidedId(String providedId);
    boolean existsByUsername(String newUsername);
    boolean existsByEmail(String email);

}
