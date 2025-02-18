package com.roomify.detection_be.service.oauth2.security;

import com.roomify.detection_be.Repository.GithubUserRepository;
import com.roomify.detection_be.Repository.RoleRepository;
import com.roomify.detection_be.Repository.UserRepository;
import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.exception.BaseException;
import com.roomify.detection_be.service.oauth2.OAuth2UserDetails;
import com.roomify.detection_be.service.oauth2.OAuth2UserDetailsFactory;
import com.roomify.detection_be.web.entity.Provider;
import com.roomify.detection_be.web.entity.Role;
import com.roomify.detection_be.web.entity.Users.GithubUser;
import com.roomify.detection_be.web.entity.Users.User;
import com.roomify.detection_be.web.service.UsernameGenerator;

import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService {
    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserDetailsService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UsernameGenerator usernameGenerator;
    private final GithubUserRepository githubUserRepository;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return checkingOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;

        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User checkingOAuth2User(
            OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserDetails oAuth2UserDetails =
                OAuth2UserDetailsFactory.createOAuth2UserDetails(
                        oAuth2User.getAttributes(),
                        oAuth2UserRequest.getClientRegistration().getRegistrationId());
        if (ObjectUtils.isEmpty(oAuth2UserDetails)) {
            throw new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND,  "Not found user in property");
        }
        String username = "";
        String githubUsername = "";
        Optional<User> user = Optional.empty();
        if (oAuth2UserRequest
                .getClientRegistration()
                .getRegistrationId()
                .equals(Provider.github.name())) {
            githubUsername = (String) oAuth2User.getAttributes().get("login");
            user = userRepository.findByGithubUserGithubUsername(githubUsername);
            username = usernameGenerator.generateUniqueUsername();
        } else if (oAuth2UserRequest
                .getClientRegistration()
                .getRegistrationId()
                .equals(Provider.google.name())) {
            user =
                    userRepository.findByEmailAndProvidedId(
                            oAuth2UserDetails.getEmail(), Provider.google.name());
            username = usernameGenerator.generateUniqueUsername();
        }
        User userDetail;
        if (user.isPresent()) {
            userDetail = user.get();
            if (!userDetail
                    .getProvidedId()
                    .equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())) {
                throw new BaseException("403", "Invalid site login with" + userDetail.getProvidedId());
            }
            userDetail = updateOAuth2UserDetails(userDetail, oAuth2UserDetails);
        } else
            userDetail =
                    registerNewOAuthUserDetails(
                            oAuth2UserDetails, oAuth2UserRequest, username, githubUsername);
        return new OAuth02UserDetailsCustom(
                userDetail.getPassword(),
                userDetail.getUsername(),
                userDetail.getUserId(),
                new SimpleGrantedAuthority(userDetail.getRole().getName()),
                oAuth2UserRequest.getClientRegistration().getRegistrationId());
    }

    private User registerNewOAuthUserDetails(
            OAuth2UserDetails oAuth2UserDetails,
            OAuth2UserRequest oAuth2UserRequest,
            String username,
            String githubUsername) {
        Optional<Role> userRole = roleRepository.findByName("USER");

        User user = User.builder()
                .username(username)
                .email(oAuth2UserDetails.getEmail() != null ? oAuth2UserDetails.getEmail() : null)
                .providedId(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                .isEnabled(true)
                .credentialsNonExpired(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .role(userRole.orElse(null))
                .build();

        user = userRepository.save(user);

        if (githubUsername != null && !githubUsername.isEmpty()) {
            GithubUser githubUser = GithubUser.builder()
                    .githubUsername(githubUsername)
                    .user(user)
                    .build();

            githubUserRepository.save(githubUser);
        }

        return user;
    }


    private User updateOAuth2UserDetails(
            User user, OAuth2UserDetails oAuth2UserDetails) {
        user.setEmail(oAuth2UserDetails.getEmail());
        return userRepository.save(user);
    }
}
