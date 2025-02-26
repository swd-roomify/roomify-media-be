package com.roomify.detection_be.constants;

public interface WebSocketPath {
  String WEBSOCKET_ENDPOINT = "/ws";
  String APP = "/app";
  String TOPIC = "/topic";
  String ERRORS = "/errors";
  String ROOM = "/room";

  String PATH = "/move";
  String JOIN = "/join";
  String LEAVE = "/leave";
  String CHAT = "/chat";

  String TOPIC_POSITION = TOPIC + "/positions";
  String TOPIC_CHAT = TOPIC + CHAT;
  String TOPIC_JOIN = TOPIC + JOIN;
  String TOPIC_LEAVE = TOPIC + LEAVE;
  String TOPIC_ERRORS = TOPIC + ERRORS;
}
