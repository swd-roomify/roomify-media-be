package com.roomify.detection_be.service.oauth2.handler;

import com.roomify.detection_be.service.jwt.JwtTokenSyncOAuth2;
import com.roomify.detection_be.service.oauth2.security.OAuth02UserDetailsCustom;
import com.roomify.detection_be.web.controller.api.UserController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import static com.roomify.detection_be.constants.SecurityConstants.FRONTEND_CALLBACK_URL;
import static com.roomify.detection_be.constants.SecurityConstants.TOKEN_QUERY_PARAM;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtTokenSyncOAuth2 jwtTokenSyncService;
    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth02UserDetailsCustom oauthUser = (OAuth02UserDetailsCustom) authentication.getPrincipal();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthUser.getProvider(), oauthUser.getUsername());
        log.info("Authorized client: {}, with username: {}", client, oauthUser.getUsername());
        OAuth2AccessToken accessToken = client.getAccessToken();
        String appJwtToken = jwtTokenSyncService.generateSynchronizedToken(oauthUser, accessToken);

        String frontendUrl = String.format(FRONTEND_CALLBACK_URL + TOKEN_QUERY_PARAM,
                URLEncoder.encode(appJwtToken, StandardCharsets.UTF_8));

        response.sendRedirect(frontendUrl);
    }
}
