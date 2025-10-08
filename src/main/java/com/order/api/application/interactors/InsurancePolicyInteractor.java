package com.order.api.application.interactors;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;

public class InsurancePolicyInteractor implements CreateInsurancePolicy {
    private final InsurancePolicyGateway insurancePolicyGateway;
    public InsurancePolicyInteractor(InsurancePolicyGateway insurancePolicyGateway) {
        this.insurancePolicyGateway = insurancePolicyGateway;
    }

    public InsurancePolicy create(InsurancePolicy insurancePolicy) {
        return insurancePolicyGateway.createInsurancePolicy(insurancePolicy);
    }
}
