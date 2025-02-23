package com.roomify.detection_be.service.jwt;

import com.roomify.detection_be.config.JwtConfig;
import com.roomify.detection_be.exception.BaseException;
import com.roomify.detection_be.service.basicOauth.UserDetailsCustom;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;

import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    @Override
    public SecretKey getKey() {
        byte[] key = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public String generateToken(UserDetailsCustom userDetailsCustom) {

        Instant now = Instant.now();

        List<String> roles = new ArrayList<>();

        userDetailsCustom
                .getAuthorities()
                .forEach(
                        role -> {
                            roles.add(role.getAuthority());
                        });

        log.info("Roles: {} ", roles);

        return Jwts.builder()
                .subject(userDetailsCustom.getUsername())
                .claim(
                        "authorities",
                        userDetailsCustom.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .claim("roles", roles)
                .claim("isEnable", userDetailsCustom.isEnabled())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtConfig.getExpiration())))
                .signWith(getKey())
                .compact();
    }

    @Override
    public void revokeToken(String token) {
        Claims claims = extractAllClaims(token);
        long expirationTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        tokenBlacklistService.blacklistToken(token, expirationTime);
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            final String username = extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            return !ObjectUtils.isEmpty(userDetails)
                    && !isTokenExpired(token)
                    && !tokenBlacklistService.isTokenBlacklisted(token);
        } catch (Exception e) {
            return false;
        }
    }

    private String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token expiration");
        } catch (UnsupportedJwtException e) {
            throw new BaseException(
                    String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token's not supported");
        } catch (MalformedJwtException e) {
            throw new BaseException(
                    String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format 3 part of token");
        } catch (SignatureException e) {
            throw new BaseException(
                    String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format token");
        } catch (Exception e) {
            throw new BaseException(
                    String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getLocalizedMessage());
        }

        return claims;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public String generateTokenWithCustomClaims(
            String username,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> claims,
            long expirationTimestamp) {
        List<String> roles = new ArrayList<>();
        authorities.forEach(
                role -> {
                    roles.add(role.getAuthority());
                });

        return Jwts.builder()
                .subject(username)
                .claim(
                        "authorities",
                        authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("roles", roles)
                .claim("claims", claims)
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(expirationTimestamp))
                .signWith(getKey())
                .compact();
    }

    public String generateOAuth2Token(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtConfig.getExpiration())))
                .signWith(getKey())
                .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
