package com.roomify.detection_be.web.service.database;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.roomify.detection_be.Repository.FriendShipRepository;
import com.roomify.detection_be.Repository.UserRepository;
import com.roomify.detection_be.service.basicOauth.UserDetailsCustom;
import com.roomify.detection_be.service.basicOauth.UserServiceOauth;
import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.utility.jwt.JwtTokenProvider;
import com.roomify.detection_be.web.dtos.jwt.TokenDTO;
import com.roomify.detection_be.web.dtos.req.UserCreateDtoReq;
import com.roomify.detection_be.web.dtos.req.UserCredentialReq;
import com.roomify.detection_be.web.dtos.req.UserGenerateReq;
import com.roomify.detection_be.web.dtos.res.AuthDtoRes;
import com.roomify.detection_be.web.dtos.res.UserDtoRes;
import com.roomify.detection_be.web.dtos.res.UserWSRes;
import com.roomify.detection_be.web.entities.Friendship;
import com.roomify.detection_be.web.entities.User;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;
  private final UserServiceOauth userServiceOauth;
  private final UserRepository userRepository;
  private final FriendShipRepository friendShipRepository;
  SnowflakeGenerator snowflake = new SnowflakeGenerator(1);

  public UserService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserServiceOauth userServiceOauth, UserRepository userRepository, FriendShipRepository friendShipRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticationManager = authenticationManager;
    this.userServiceOauth = userServiceOauth;
    this.userRepository = userRepository;
    this.friendShipRepository = friendShipRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public UserDtoRes createUser(UserCreateDtoReq userCreateDtoReq) {
    boolean emailExist = userRepository.existsByEmail(userCreateDtoReq.getEmail());
    boolean usernameExist = userRepository.existsByUsername(userCreateDtoReq.getUsername());
    if (emailExist || usernameExist) {
      throw new ResponseStatusException(
          HttpStatusCode.valueOf(400), "Email or username already exist");
    }

    userCreateDtoReq.setPassword(
        BCrypt.withDefaults().hashToString(12, userCreateDtoReq.getPassword().toCharArray()));
    User user = new User();
    user.setUserId(String.valueOf(snowflake.nextId()));
    user.setEmail(userCreateDtoReq.getEmail());
    user.setUsername(userCreateDtoReq.getUsername());
    user.setPassword(userCreateDtoReq.getPassword());
    User savedUser = userRepository.save(user);
    return new UserDtoRes(
        savedUser.getUserId(),
        savedUser.getUsername(),
        savedUser.getEmail(),
        savedUser.getCreatedAt());
  }

  public AuthDtoRes GetUserAuthorize(UserCredentialReq userCredentialReq) {
    User user =
        userRepository
            .findByEmail(userCredentialReq.getEmail())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found"));
    if(user.getPassword() == null){
      throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Incorrect login method, password is not initialized here yet");
    }
    if (!BCrypt.verifyer()
        .verify(userCredentialReq.getPassword().toCharArray(), user.getPassword())
        .verified) {
      throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Incorrect password");
    }
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userCredentialReq.getEmail(), userCredentialReq.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtTokenProvider.generateToken((UserDetailsCustom) authentication.getPrincipal());
    return AuthDtoRes.toDto(
        new TokenDTO(token),
        new UserDtoRes(user.getUserId(), user.getUsername(), user.getEmail(), user.getCreatedAt()));
  }

  public UserWSRes GenerateCharacter(UserGenerateReq user) {
    return new UserWSRes(user.getUserId(), user.getUsername(), user.getCharacter(), 400, 400);
  }

  public String generateFriendShip(String userIdUser2) {
    Friendship friendship = new Friendship();
    Optional<User> user = userServiceOauth.findCurrentUser();
    if (user.isPresent()) {
      friendship.setUser1(userRepository.findById(user.get().getUserId()).orElseThrow());
      friendship.setUser2(userRepository.findById(userIdUser2).orElseThrow());
      friendship.setStatus("PENDING");
      friendShipRepository.save(friendship);
    }
    return "Friend request sent.";
  }

  public ResponseEntity<String> removeFriendShip(String userIdUser2) {
    Optional<User> user1 = userServiceOauth.findCurrentUser();
    if (user1.isPresent()) {
      Friendship friendship =
          friendShipRepository
              .findByUser1AndUser2(user1, userRepository.findById(userIdUser2))
              .orElseThrow(() -> new RuntimeException("Friendship not found"));
      friendShipRepository.delete(friendship);
    }
    return ResponseEntity.ok("Unfriended successfully.");
  }
}
