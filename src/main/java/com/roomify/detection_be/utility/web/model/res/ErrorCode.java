package com.roomify.detection_be.utility.web.model.res;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ErrorCode {
  Integer getHttpStatusCode();

  @JsonValue
  String getSystemCode();
}
