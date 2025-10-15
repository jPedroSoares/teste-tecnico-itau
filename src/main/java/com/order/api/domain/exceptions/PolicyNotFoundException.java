package com.order.api.domain.exceptions;

import java.util.UUID;

public class PolicyNotFoundException extends RuntimeException {
    
    public PolicyNotFoundException(UUID policyId) {
        super("Insurance policy not found with ID: " + policyId);
    }
    
    public PolicyNotFoundException(String field, Object value) {
        super("Insurance policy not found with " + field + ": " + value);
    }
}