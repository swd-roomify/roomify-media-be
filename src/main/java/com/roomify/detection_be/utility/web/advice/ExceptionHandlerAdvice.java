package com.roomify.detection_be.utility.web.advice;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.roomify.detection_be.utility.StringFormatter;
import com.roomify.detection_be.utility.web.model.res.ApiResp;
import com.roomify.detection_be.utility.web.model.res.ErrorCode;
import com.roomify.detection_be.utility.web.model.res.FieldError;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Getter
public abstract class ExceptionHandlerAdvice {
  private final MessageSource messageSource;

  public ExceptionHandlerAdvice(final MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public List<FieldError> extractFieldErrors(final BindingResult bindingResult) {
    List<FieldError> fieldErrors = Collections.emptyList();
    final String VALUE_PATTERN = "\\{value}";
    final String FIELD_NAME_PATTERN = "\\{fieldName}";

    if (bindingResult.hasFieldErrors()) {
      fieldErrors =
          bindingResult.getFieldErrors().stream()
              .map(
                  error -> {
                    String message = null;
                    if (error.getDefaultMessage() != null) {
                      message =
                          error
                              .getDefaultMessage()
                              .replaceFirst(
                                  FIELD_NAME_PATTERN,
                                  StringFormatter.translateCamelCaseToHumanReadable(
                                      error.getField()))
                              .replaceFirst(
                                  VALUE_PATTERN, String.valueOf(error.getRejectedValue()));
                    }
                    return FieldError.builder()
                        .field(
                            new PropertyNamingStrategies.SnakeCaseStrategy()
                                .translate(error.getField()))
                        .message(message)
                        .build();
                  })
              .toList();
    }

    return fieldErrors;
  }

  public ResponseEntity<?> error(final ErrorCode errorCode, final List<FieldError> fieldErrors) {
    var message =
        messageSource.getMessage(errorCode.getSystemCode(), null, LocaleContextHolder.getLocale());

    var error =
        ApiResp.ErrorResp.builder().code(errorCode).message(message).details(fieldErrors).build();

    var response = ApiResp.builder().error(error).build();

    return ResponseEntity.status(error.getCode().getHttpStatusCode()).body(response);
  }

  public ResponseEntity<?> error(final ErrorCode errorCode, final Object... args) {
    var message =
        messageSource.getMessage(errorCode.getSystemCode(), args, LocaleContextHolder.getLocale());

    var error = ApiResp.ErrorResp.builder().code(errorCode).message(message).build();

    var response = ApiResp.builder().error(error).build();

    return ResponseEntity.status(error.getCode().getHttpStatusCode()).body(response);
  }

  public ResponseEntity<?> error(
      final List<ErrorCode> errorCodes, final FieldError details, final Object... args) {
    var message =
        messageSource.getMessage(
            errorCodes.get(0).getSystemCode(), null, LocaleContextHolder.getLocale());

    var detailsMessage =
        messageSource.getMessage(
            errorCodes.get(1).getSystemCode(), args, LocaleContextHolder.getLocale());

    details.setMessage(detailsMessage);

    var error =
        ApiResp.ErrorResp.builder()
            .code(errorCodes.get(0))
            .message(message)
            .details(details)
            .build();

    var response = ApiResp.builder().error(error).build();

    return ResponseEntity.status(error.getCode().getHttpStatusCode()).body(response);
  }
}
