package com.projeto.erp.exception;

import org.springframework.http.HttpStatus;

public record ApiError(
        int status,
        String error,
        String message,
        String path
) {

    public ApiError(HttpStatus status, String message, String path) {
        this(
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

    public ApiError(String message) {
        this(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                null
        );
    }
}
