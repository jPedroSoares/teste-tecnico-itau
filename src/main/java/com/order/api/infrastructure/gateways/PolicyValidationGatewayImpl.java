package com.order.api.infrastructure.gateways;

import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.dto.PolicyValidationResponse;
import com.order.api.application.gateways.PolicyValidationGateway;
import org.springframework.web.client.RestClient;

public class PolicyValidationGatewayImpl implements PolicyValidationGateway {
    private final RestClient restClient;

    public PolicyValidationGatewayImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public PolicyValidationResponse validate(PolicyValidationRequest policyValidationRequest) {
        return restClient.post()
                .uri("/check")
                .body(policyValidationRequest)
                .retrieve()
                .body(PolicyValidationResponse.class);
    }
}
