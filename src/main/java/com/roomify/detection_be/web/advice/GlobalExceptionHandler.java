package com.roomify.detection_be.web.advice;

import com.roomify.detection_be.exception.BaseException;
import com.roomify.detection_be.exception.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ExceptionResponse> handleBaseException(BaseException ex) {
    ExceptionResponse response = new ExceptionResponse(ex.getCode(), ex.getMessage());
    return ResponseEntity.status(Integer.parseInt(ex.getCode())).body(response);
  }
}
