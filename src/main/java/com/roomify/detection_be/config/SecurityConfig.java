package com.roomify.detection_be.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomify.detection_be.constants.SecurityConstants;
import com.roomify.detection_be.exception.CustomAccessDeniedHandler;
import com.roomify.detection_be.service.UserDetailsServiceCustom;
import com.roomify.detection_be.service.jwt.*;
import com.roomify.detection_be.service.oauth2.handler.CustomOAuth2FailureHandler;
import com.roomify.detection_be.service.oauth2.handler.CustomOAuth2SuccessHandler;
import com.roomify.detection_be.service.oauth2.security.CustomOAuth2UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final CustomOAuth2UserDetailsService customOAuth2UserDetailsService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtTokenSyncOAuth2 jwtTokenSyncService;
    private final ObjectMapper objectMapper;

    @Bean
    public CustomOAuth2SuccessHandler customOAuth2SuccessHandler() {
        return new CustomOAuth2SuccessHandler(authorizedClientService, jwtTokenSyncService,objectMapper);
    }

    @Bean
    public CustomOAuth2FailureHandler customOAuth2FailureHandler() {
        return new CustomOAuth2FailureHandler(objectMapper);
    }

    @Autowired
    public void configGlobal(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authManager = authenticationManager(http);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityConstants.PUBLIC_URLS).permitAll()
                        .requestMatchers(SecurityConstants.WEBSOCKET_URLS).permitAll()
                        .requestMatchers(SecurityConstants.ADMIN_URL_PREFIX).hasAuthority(SecurityConstants.ROLE_ADMIN)
                        .requestMatchers(SecurityConstants.USER_URL_PREFIX).hasAuthority(SecurityConstants.ROLE_USER)
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage(SecurityConstants.ACCESS_DENIED_PAGE)
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl(SecurityConstants.FRONTEND_CALLBACK_URL, true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserDetailsService)
                        )
                        .successHandler(customOAuth2SuccessHandler())
                        .failureHandler(customOAuth2FailureHandler())
                )
                .formLogin(form -> form
                        .defaultSuccessUrl(SecurityConstants.FRONTEND_CALLBACK_URL, true)
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .deleteCookies(SecurityConstants.SESSION_COOKIE)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher(SecurityConstants.LOGOUT_URL))
                        .logoutSuccessUrl(SecurityConstants.LOGIN_SUCCESS_URL)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtUsernamePasswordAuthenticationFilter(authManager, jwtConfig, jwtService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        new JwtTokenAuthenticationFilter(jwtConfig, jwtService),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}