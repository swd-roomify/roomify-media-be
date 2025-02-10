package com.roomify.detection_be.web.service.database;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.roomify.detection_be.repository.UserRepository;
import com.roomify.detection_be.utility.SnowflakeGenerator;
import com.roomify.detection_be.utility.jwt.JwtTokenProvider;
import com.roomify.detection_be.web.dtos.jwt.CustomUserDetailsDTO;
import com.roomify.detection_be.web.dtos.jwt.TokenDTO;
import com.roomify.detection_be.web.dtos.req.UserCreateDtoReq;
import com.roomify.detection_be.web.dtos.req.UserCredentialReq;
import com.roomify.detection_be.web.dtos.res.AuthDtoRes;
import com.roomify.detection_be.web.dtos.res.UserDtoRes;
import com.roomify.detection_be.web.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    SnowflakeGenerator snowflake = new SnowflakeGenerator(1);

    public UserService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UserDtoRes CreateUser(UserCreateDtoReq userCreateDtoReq) {
        User emailExist = userRepository.findByEmail(userCreateDtoReq.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Email already exist"));
        User usernameExist = userRepository.findByUsername(userCreateDtoReq.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Username already exist"));

        userCreateDtoReq.setPassword(BCrypt.withDefaults().hashToString(12, userCreateDtoReq.getPassword().toCharArray()));
        User user = new User();
        user.setUserId(String.valueOf(snowflake.nextId()));
        user.setEmail(userCreateDtoReq.getEmail());
        user.setUsername(userCreateDtoReq.getUsername());
        user.setPassword(userCreateDtoReq.getPassword());
        User savedUser = userRepository.save(user);
        return new UserDtoRes(savedUser.getUserId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getCreatedAt());
    }

    public AuthDtoRes GetUserAuthorize(UserCredentialReq userCredentialReq) {
        User user = userRepository.findByEmail(userCredentialReq.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404),"User not found"));
        if (!BCrypt.verifyer().verify(userCredentialReq.getPassword().toCharArray(), user.getPassword()).verified) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403),"Incorrect password");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentialReq.getEmail(), userCredentialReq.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken((CustomUserDetailsDTO) authentication.getPrincipal());
        return AuthDtoRes.toDto(new TokenDTO(token), new UserDtoRes(user.getUserId(), user.getUsername(), user.getEmail(), user.getCreatedAt()));
    }
}
