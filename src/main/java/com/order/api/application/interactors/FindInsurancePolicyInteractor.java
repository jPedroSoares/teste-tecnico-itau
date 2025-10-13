package com.order.api.application.interactors;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;

import java.util.List;
import java.util.UUID;

public record FindInsurancePolicyInteractor(
        InsurancePolicyGateway insurancePolicyGateway
) implements FindInsurancePolicy {
    public InsurancePolicy find(UUID id) {
        return insurancePolicyGateway.findById(id);
    }
    public List<InsurancePolicy> findByCustomerId(UUID customerId) {
        return insurancePolicyGateway.findByCustomerId(customerId);
    }
}
