package com.order.api.application.gateways;

import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.dto.PolicyValidationResponse;

public interface PolicyValidationGateway {
    PolicyValidationResponse validate(PolicyValidationRequest policyValidationRequest);
}
