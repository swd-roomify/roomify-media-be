package com.roomify.detection_be.service.oauth2;

import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.web.entities.Provider;
import java.util.Map;

public class OAuth2UserDetailsFactory {
  public static OAuth2UserDetails createOAuth2UserDetails(
      Map<String, Object> attributes, String registrationId) {
    if (registrationId.equals(Provider.google.name())) {
      return new OAuth2UserDetailsGoogle(attributes);
    } else if (registrationId.equals(Provider.github.name())) {
      return new OAuth2UserDetailsGithub(attributes);
    } else
      throw new ApplicationException(ApplicationErrorCode.INTERNAL_ERROR_SERVER, registrationId);
  }
}
