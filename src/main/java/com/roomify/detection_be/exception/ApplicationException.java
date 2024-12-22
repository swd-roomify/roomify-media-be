package com.roomify.detection_be.exception;

import com.roomify.detection_be.utility.web.model.res.ErrorCode;
import lombok.Getter;

/**
 * @author DigiEx
 */
@Getter
public class ApplicationException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Object[] args;

  public ApplicationException(ErrorCode errorCode, Object... args) {
    this.errorCode = errorCode;
    this.args = args;
  }
}
