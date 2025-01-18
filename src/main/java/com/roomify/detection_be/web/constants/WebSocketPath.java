package com.roomify.detection_be.web.constants;

public interface WebSocketPath {
  String WEBSOCKET_ENDPOINT = "/ws";
  String APP = "/app";
  String TOPIC = "/topic";
  String ERRORS = "/errors";

  String PATH = "/move";
  String JOIN = "/join";

  String TOPIC_POSITION = TOPIC + "/positions";
  String TOPIC_ERRORS = TOPIC + ERRORS;
}
