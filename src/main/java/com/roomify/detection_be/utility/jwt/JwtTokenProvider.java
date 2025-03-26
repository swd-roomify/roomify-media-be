package com.roomify.detection_be.utility.jwt;

import com.roomify.detection_be.service.basicOauth.UserDetailsCustom;
import com.roomify.detection_be.web.dtos.jwt.CustomUserDetailsDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationDate;

  public String generateToken(UserDetailsCustom authentication) {
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
      Claims claims = claimsJws.getPayload();

      if (claims.getExpiration().before(new Date())) {
        System.out.println("JWT expired");
        return false;
      }

      String expectedIssuer = "your-issuer";
      String expectedAudience = "your-audience";
      if (!expectedIssuer.equals(claims.getIssuer()) || !expectedAudience.equals(claims.getAudience())) {
        System.out.println("Invalid issuer or audience");
        return false;
      }
      return true;
    } catch (ExpiredJwtException e) {
      System.out.println("JWT expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.out.println("Unsupported JWT: " + e.getMessage());
    } catch (MalformedJwtException e) {
      System.out.println("Malformed JWT: " + e.getMessage());
    } catch (SignatureException e) {
      System.out.println("Invalid signature: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid token: " + e.getMessage());
    }
    return false;
  }

}
