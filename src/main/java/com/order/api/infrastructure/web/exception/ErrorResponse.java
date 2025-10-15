package com.order.api.infrastructure.web.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        String message,
        String path,
        int status,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String error, String message, String path, int status) {
        return new ErrorResponse(error, message, path, status, LocalDateTime.now());
    }
}