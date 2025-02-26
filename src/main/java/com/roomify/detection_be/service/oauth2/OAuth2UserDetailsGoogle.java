package com.roomify.detection_be.service.oauth2;

import java.util.Map;

public class OAuth2UserDetailsGoogle extends OAuth2UserDetails {
  public OAuth2UserDetailsGoogle(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }
}
