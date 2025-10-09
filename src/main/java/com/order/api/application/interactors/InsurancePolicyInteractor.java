package com.order.api.application.interactors;

import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;

public record InsurancePolicyInteractor(
        InsurancePolicyGateway insurancePolicyGateway,
        PolicyValidationGateway policyValidationGateway
) implements CreateInsurancePolicy {

    public InsurancePolicy create(InsurancePolicy insurancePolicy) {
        InsurancePolicy createdInsurancePolicy = insurancePolicyGateway.createInsurancePolicy(insurancePolicy);
        PolicyValidationRequest policyValidationRequest = new PolicyValidationRequest(createdInsurancePolicy.getId(), createdInsurancePolicy.getCustomerId());
        policyValidationGateway.validate(policyValidationRequest);
        return createdInsurancePolicy;
    }
}
