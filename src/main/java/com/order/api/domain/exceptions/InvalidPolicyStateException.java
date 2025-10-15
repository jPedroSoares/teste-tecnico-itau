package com.order.api.domain.exceptions;

import com.order.api.domain.enums.InsurancePolicyStatus;

public class InvalidPolicyStateException extends RuntimeException {
    
    public InvalidPolicyStateException(String operation, InsurancePolicyStatus currentState) {
        super("Cannot " + operation + " a policy in " + currentState + " state.");
    }
    
    public InvalidPolicyStateException(String message) {
        super(message);
    }
}