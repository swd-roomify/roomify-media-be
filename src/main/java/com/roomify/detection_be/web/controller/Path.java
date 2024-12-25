package com.roomify.detection_be.web.controller;

public interface Path {
  String WEBSOCKET_ENDPOINT = "/ws";
  String APP = "/app";
  String TOPIC = "/topic";

  String PATH = "/move";
  String JOIN = "/join";

  String POSITION = TOPIC + "/positions";
}
