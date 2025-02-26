package com.roomify.detection_be.service.jwt;

import com.roomify.detection_be.config.JwtConfig;
import com.roomify.detection_be.dto.BaseResponseDTO;
import com.roomify.detection_be.dto.HelperUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtConfig jwtConfig;

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String accessToken = request.getHeader(jwtConfig.getHeader());
    log.info("Start do filter once per request, {}", request.getRequestURI());

    if (!ObjectUtils.isEmpty(accessToken) && accessToken.startsWith(jwtConfig.getPrefix() + " ")) {
      accessToken = accessToken.substring((jwtConfig.getPrefix() + " ").length());
      log.info("passed the prefix test");
      try {
        if (jwtService.isValidToken(accessToken)) {
          log.info("passed the valid token test");
          Claims claims = jwtService.extractClaims(accessToken);

          String username = claims.getSubject();

          List<String> authorities = claims.get("authorities", List.class);

          if (!ObjectUtils.isEmpty(username)) {
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("passed the authentication test");
          }
        } else {
          log.info("Failed the valid token test");
        }
      } catch (Exception e) {
        log.error(
            "Error on filter once per request, path {}, error: {}",
            request.getRequestURI(),
            e.getMessage());
        log.warn(
            "Error on filter once per request, path {}, error: {}",
            request.getRequestURI(),
            e.getMessage());
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        responseDTO.setMessage(e.getLocalizedMessage());

        String json = HelperUtils.JSON_WRITER.writeValueAsString(responseDTO);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
        return;
      }
    }
    log.info("end do filter: {}", request.getRequestURI());
    filterChain.doFilter(request, response);
  }
}
