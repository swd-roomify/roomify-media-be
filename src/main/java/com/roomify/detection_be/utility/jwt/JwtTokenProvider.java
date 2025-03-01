package com.roomify.detection_be.utility.jwt;

import com.roomify.detection_be.web.dtos.jwt.CustomUserDetailsDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${{jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationDate; // Changed to long

  public String generateToken(CustomUserDetailsDTO authentication) {
    String username = authentication.getUsername();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
    return Jwts.builder()
        .subject(username)
        .issuedAt(currentDate)
        .expiration(expireDate)
        .signWith(key())
        .compact();
  }

  private SecretKey key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUsername(String token) {
    Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
      return !claimsJws.getPayload().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
