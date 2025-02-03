package com.roomify.detection_be.service.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomify.detection_be.service.jwt.JwtService;
import com.roomify.detection_be.service.jwt.JwtTokenSyncOAuth2;
import com.roomify.detection_be.service.oauth2.security.OAuth02UserDetailsCustom;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtTokenSyncOAuth2 jwtTokenSyncService;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth02UserDetailsCustom oauthUser = (OAuth02UserDetailsCustom) authentication.getPrincipal();

        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(
                        oauthUser.getProvider(), oauthUser.getUsername());

        OAuth2AccessToken accessToken = client.getAccessToken();

        String appJwtToken = jwtTokenSyncService.generateSynchronizedToken(oauthUser, accessToken);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(objectMapper.writeValueAsString(appJwtToken));

    }
}
