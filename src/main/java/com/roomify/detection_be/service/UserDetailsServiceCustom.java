package com.roomify.detection_be.service;

import com.roomify.detection_be.Repository.UserRepository;
import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.exception.BaseException;
import com.roomify.detection_be.web.entity.Users.User;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsCustom userDetailsCustom = getUserDetailsCustom(username);

        if (ObjectUtils.isEmpty(userDetailsCustom)) {
            throw new ApplicationException(ApplicationErrorCode.USER_NOT_FOUND, "User not found");
        }
        return userDetailsCustom;
    }

    private UserDetailsCustom getUserDetailsCustom(String username) {
        return userRepository.findByUsername(username)
                .map(_user -> new UserDetailsCustom(
                        _user.getUsername(),
                        _user.getPassword(),
                        List.of(new SimpleGrantedAuthority(_user.getRole().getName())),
                        _user.isEnabled(),
                        _user.isAccountNonExpired(),
                        _user.isAccountNonLocked(),
                        _user.isCredentialsNonExpired()
                ))
                .orElseThrow(() -> new ApplicationException(
                        ApplicationErrorCode.USER_NOT_FOUND,
                        "User not found"
                ));
    }
}
