package com.roomify.detection_be.service.oauth2.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth02UserDetailsCustom implements OAuth2User, UserDetails {
  private String id;
  private String username;
  private String password;
  private Map<String, Object> attributes;
  private List<GrantedAuthority> authorities;
  private String providerId;
  private boolean isEnabled;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  public OAuth02UserDetailsCustom(
      String password, String username, String id, GrantedAuthority authority, String providerId) {
    this.password = password;
    this.username = username;
    this.id = id;
    this.authorities = List.of(authority);
    this.providerId = providerId;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getName() {
    return String.valueOf(id);
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  @Override
  public <A> A getAttribute(String name) {
    return OAuth2User.super.getAttribute(name);
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  public String getProvider() {
    return providerId;
  }
}
