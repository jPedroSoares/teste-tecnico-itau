package com.order.api.infrastructure.gateways;

import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.dto.PolicyValidationResponse;
import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.domain.exceptions.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

@Slf4j
public class PolicyValidationGatewayImpl implements PolicyValidationGateway {
    private final RestClient restClient;

    public PolicyValidationGatewayImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public PolicyValidationResponse validate(PolicyValidationRequest policyValidationRequest) {
        log.info("Calling anti-fraud API: policyId={}, customerId={}",
                policyValidationRequest.orderId(), policyValidationRequest.customerId());
        try {
            return restClient.post()
                    .uri("/check")
                    .body(policyValidationRequest)
                    .retrieve()
                    .body(PolicyValidationResponse.class);
        } catch (Exception e) {
            log.error("Anti-fraud API call failed: policyId={}, customerId={}, error={}",
                    policyValidationRequest.orderId(), policyValidationRequest.customerId(), e.getMessage(), e);
            throw new ExternalServiceException("Anti-fraud API", "Failed to validate policy", e);
        }
    }
}
