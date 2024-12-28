package com.roomify.detection_be.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class CustomCorsFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    // Log origin and headers for debugging

    log.info("Request Origin: {}", request.getHeader("Origin"));
    log.info("Requested URL: {}", request.getRequestURL());

    // Check if the headers are set correctly
    response.setHeader("Access-Control-Allow-Origin", "http://pog.threemusketeer.click:5173");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    filterChain.doFilter(request, response);
  }
}
