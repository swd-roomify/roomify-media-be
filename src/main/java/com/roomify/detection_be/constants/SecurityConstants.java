package com.roomify.detection_be.constants;

public interface SecurityConstants {
  String[] PUBLIC_URLS = {
    "/api/v1/register-account",
    "/oauth2/authorization/google",
    "/oauth2/authorization/github",
    "/login/oauth2/code/*",
    "/oauth2/**",
    "/grantcode",
    "/api/v1/login/non-type",
    "/api/auth/**"
  };

  String[] WEBSOCKET_URLS = {"/ws/**", "/ws/info/**", "/topic/**"};

  String ADMIN_URL_PREFIX = "/admin/**";
  String USER_URL_PREFIX = "/user/**";
  String FRONTEND_CALLBACK_URL =
      System.getenv("FRONTEND_CALLBACK_URL") != null
          ? System.getenv("FRONTEND_CALLBACK_URL")
          : "http://localhost:5173/api/auth/oauth2/success";
  String ERROR_QUERY_PARAM = "?errorMessage=%s";
  String TOKEN_QUERY_PARAM = "?token=%s";
  String LOGOUT_URL = "/logout";
  String LOGIN_SUCCESS_URL = "/login?logout";
  String ACCESS_DENIED_PAGE = "/403";

  String ROLE_ADMIN = "ADMIN";
  String ROLE_USER = "USER";

  String SESSION_COOKIE = "JSESSIONID";

  String[] ALLOWED_ORIGINS = {"http://pog.threemusketeer.click:5173", "http://localhost:5173"};
  long CORS_MAX_AGE = 3600;
}
