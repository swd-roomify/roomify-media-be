package com.roomify.detection_be.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
  public NotFoundException(ApplicationErrorCode errorCode, Object... args) {
    this.errorCode = errorCode;
    this.args = args;
  }

  private final ApplicationErrorCode errorCode;

  private final Object[] args;
}
