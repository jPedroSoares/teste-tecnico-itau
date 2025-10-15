package com.order.api.infrastructure.web.exception;

import com.order.api.domain.exceptions.ExternalServiceException;
import com.order.api.domain.exceptions.InvalidPolicyStateException;
import com.order.api.domain.exceptions.PolicyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePolicyNotFoundException(
            PolicyNotFoundException ex, WebRequest request) {
        log.error("Policy not found: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                "POLICY_NOT_FOUND",
                "The requested insurance policy was not found",
                getPath(request),
                HttpStatus.NOT_FOUND.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPolicyStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPolicyStateException(
            InvalidPolicyStateException ex, WebRequest request) {
        log.error("Invalid policy state operation: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                "INVALID_POLICY_STATE",
                "The requested operation cannot be performed in the current policy state",
                getPath(request),
                HttpStatus.CONFLICT.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(
            ExternalServiceException ex, WebRequest request) {
        log.error("External service error: {}", ex.getMessage(), ex);
        
        ErrorResponse error = ErrorResponse.of(
                "EXTERNAL_SERVICE_ERROR",
                "Unable to process request due to external service unavailability",
                getPath(request),
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        log.error("Invalid argument: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                "INVALID_ARGUMENT",
                "Invalid request parameters",
                getPath(request),
                HttpStatus.BAD_REQUEST.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                "VALIDATION_ERROR",
                "Request validation failed",
                getPath(request),
                HttpStatus.BAD_REQUEST.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        log.error("Malformed request body: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                "MALFORMED_REQUEST",
                "Request body is malformed or invalid",
                getPath(request),
                HttpStatus.BAD_REQUEST.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse error = ErrorResponse.of(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later",
                getPath(request),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}