package com.order.api.application.interactors;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.exceptions.PolicyNotFoundException;
import com.order.api.domain.usecases.FindInsurancePolicy;

import java.util.List;
import java.util.UUID;

public record FindInsurancePolicyInteractor(
        InsurancePolicyGateway insurancePolicyGateway
) implements FindInsurancePolicy {
    public InsurancePolicy find(UUID id) {
        InsurancePolicy policy = insurancePolicyGateway.findById(id);
        if (policy == null) {
            throw new PolicyNotFoundException(id);
        }
        return policy;
    }
    
    public List<InsurancePolicy> findByCustomerId(UUID customerId) {
        List<InsurancePolicy> policies = insurancePolicyGateway.findByCustomerId(customerId);
        if (policies.isEmpty()) {
            throw new PolicyNotFoundException("customerId", customerId);
        }
        return policies;
    }
}
