package com.roomify.detection_be.service.jwt;

import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.web.repository.UserRepository;
import com.roomify.detection_be.service.basicOauth.UserDetailsCustom;
import com.roomify.detection_be.web.entities.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String email = authentication.getPrincipal().toString();
    final String password = authentication.getCredentials().toString();

    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    new ApplicationException(
                        ApplicationErrorCode.USER_NOT_FOUND, "User not found"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new ApplicationException(ApplicationErrorCode.UNAUTHORIZED, "Invalid password");
    }

    List<GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority(user.getRole().getName()));

    UserDetailsCustom userDetails =
        new UserDetailsCustom(
            user.getUsername(),
            user.getPassword(),
            authorities,
            user.isEnabled(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired());

    return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }
}
