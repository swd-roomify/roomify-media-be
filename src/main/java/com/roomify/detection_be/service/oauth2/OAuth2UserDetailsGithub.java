package com.roomify.detection_be.service.oauth2;

import java.util.Map;

public class OAuth2UserDetailsGithub extends OAuth2UserDetails {
    public OAuth2UserDetailsGithub(Map<String, Object> attributes) {
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
