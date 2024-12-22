package com.roomify.detection_be.web.advice;

import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.exception.NotFoundException;
import com.roomify.detection_be.exception.ValidationException;
import com.roomify.detection_be.utility.web.advice.ExceptionHandlerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ExceptionHandlerAdvice {
  public ApplicationExceptionHandler(
      @Qualifier("globalMessageSource") MessageSource messageSource) {
    super(messageSource);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> handle(final HttpServletRequest request, final Throwable e) {
    log.error("{}", ExceptionUtils.getStackTrace(e));

    return error(ApplicationErrorCode.INTERNAL_ERROR_SERVER);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<?> handle(
      final HttpServletRequest request, final HttpRequestMethodNotSupportedException e) {
    return error(ApplicationErrorCode.INVALID_HTTP_REQUEST_METHOD);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handle(
      final HttpServletRequest request, final MethodArgumentNotValidException e) {
    return error(
        ApplicationErrorCode.INVALID_REQUEST_PARAMETER, extractFieldErrors(e.getBindingResult()));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<?> handle(
      final HttpServletRequest request, final NoResourceFoundException e) {
    return error(ApplicationErrorCode.INVALID_HTTP_REQUEST_RESOURCE, e.getResourcePath());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> handle(HttpServletRequest request, NotFoundException e) {
    return error(e.getErrorCode(), e.getArgs());
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<?> handle(HttpServletRequest request, ApplicationException e) {
    return error(e.getErrorCode(), e.getArgs());
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> handle(final HttpServletRequest request, final ValidationException e) {
    return error(e.getErrorCode(), e.getArgs());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<?> handle(
      final HttpServletRequest request, final MaxUploadSizeExceededException e) {
    return error(ApplicationErrorCode.MAXIMUM_PAY_LOAD);
  }
}
