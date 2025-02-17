package com.roomify.detection_be.web.advice;

import com.roomify.detection_be.exception.ApplicationErrorCode;
import com.roomify.detection_be.exception.ApplicationException;
import com.roomify.detection_be.exception.NotFoundException;
import com.roomify.detection_be.exception.ValidationException;
import com.roomify.detection_be.utility.web.model.res.ErrorCode;
import com.roomify.detection_be.constants.WebSocketPath;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
public class ApplicationWebSocketExceptionHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageSource messageSource;

    public ApplicationWebSocketExceptionHandler(
            SimpMessagingTemplate messagingTemplate,
            @Qualifier("globalMessageSource") MessageSource messageSource) {
        this.messagingTemplate = messagingTemplate;
        this.messageSource = messageSource;
    }

    @MessageExceptionHandler(Throwable.class)
    public void handle(Throwable e) {
        log.error("{}", ExceptionUtils.getStackTrace(e));
        sendError(ApplicationErrorCode.INTERNAL_ERROR_SERVER, null);
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public void handle(MethodArgumentNotValidException e) {
        sendError(ApplicationErrorCode.INVALID_REQUEST_PARAMETER, null);
    }

    @MessageExceptionHandler(NotFoundException.class)
    public void handle(NotFoundException e) {
        sendError(e.getErrorCode(), e.getArgs());
    }

    @MessageExceptionHandler(ApplicationException.class)
    public void handle(ApplicationException e) {
        sendError(e.getErrorCode(), e.getArgs());
    }

    @MessageExceptionHandler(ValidationException.class)
    public void handle(ValidationException e) {
        sendError(e.getErrorCode(), e.getArgs());
    }

    @MessageExceptionHandler(MaxUploadSizeExceededException.class)
    public void handle(MaxUploadSizeExceededException e) {
        sendError(ApplicationErrorCode.MAXIMUM_PAY_LOAD, null);
    }

    private void sendError(ErrorCode errorCode, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage(errorCode.getSystemCode(), args, locale);
        messagingTemplate.convertAndSend(WebSocketPath.TOPIC_ERRORS, errorMessage);
    }
}
