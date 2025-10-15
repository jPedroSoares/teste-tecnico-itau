package com.order.api.domain.exceptions;

public class ExternalServiceException extends RuntimeException {
    
    public ExternalServiceException(String serviceName, String message) {
        super("External service '" + serviceName + "' failed: " + message);
    }
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super("External service '" + serviceName + "' failed: " + message, cause);
    }
}