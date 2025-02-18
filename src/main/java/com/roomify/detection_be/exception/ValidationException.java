package com.roomify.detection_be.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8231722937707194721L;

    private final Object[] args;

    private final ApplicationErrorCode errorCode;

    public ValidationException(ApplicationErrorCode errorCode, Object... args) {
        this.args = args;
        this.errorCode = errorCode;
    }
}
