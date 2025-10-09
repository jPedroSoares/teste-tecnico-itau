package com.order.api.application.interactors;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;

public record InsurancePolicyInteractor(
        InsurancePolicyGateway insurancePolicyGateway) implements CreateInsurancePolicy {

    public InsurancePolicy create(InsurancePolicy insurancePolicy) {
        return insurancePolicyGateway.createInsurancePolicy(insurancePolicy);
    }
}
