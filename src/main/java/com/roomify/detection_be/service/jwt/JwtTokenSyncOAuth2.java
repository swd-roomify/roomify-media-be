package com.roomify.detection_be.service.jwt;

import com.roomify.detection_be.service.oauth2.security.OAuth02UserDetailsCustom;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtTokenSyncOAuth2 {
  private final JwtService jwtService;

  public String generateSynchronizedToken(
      OAuth02UserDetailsCustom oauthUser, OAuth2AccessToken accessToken) {
    long tokenExpiration = accessToken.getExpiresAt().toEpochMilli();

    Map<String, Object> claims = new HashMap<>();
    claims.put("tokenExpiration", tokenExpiration);

    return jwtService.generateTokenWithCustomClaims(
        oauthUser.getUsername(), oauthUser.getAuthorities(), claims, tokenExpiration);
  }

  public boolean isTokenExpired(String token) {
    return jwtService.isTokenExpired(token);
  }
}
