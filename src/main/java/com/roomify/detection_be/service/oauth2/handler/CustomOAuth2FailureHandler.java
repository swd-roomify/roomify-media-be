package com.roomify.detection_be.service.oauth2.handler;

import static com.roomify.detection_be.constants.SecurityConstants.ERROR_QUERY_PARAM;
import static com.roomify.detection_be.constants.SecurityConstants.FRONTEND_CALLBACK_URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@RequiredArgsConstructor
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    String errorMessage = exception.getMessage();
    String frontendUrl =
        String.format(
            FRONTEND_CALLBACK_URL + ERROR_QUERY_PARAM,
            URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));

    response.sendRedirect(frontendUrl);
  }
}
