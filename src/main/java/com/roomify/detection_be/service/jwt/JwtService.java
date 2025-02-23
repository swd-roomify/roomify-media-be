package com.roomify.detection_be.service.jwt;

import com.roomify.detection_be.service.basicOauth.UserDetailsCustom;
import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

public interface JwtService {

  Claims extractClaims(String token);

  Key getKey();

  String generateToken(UserDetailsCustom userDetailsCustom);

  boolean isValidToken(String token);

  boolean isTokenExpired(String token);

  String generateTokenWithCustomClaims(
      String name,
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> claims,
      long googleTokenExpiration);

  String generateOAuth2Token(String userId);

  void revokeToken(String accessToken);
}
