package com.order.api.application.interactors;

import com.order.api.application.dto.PolicyValidationRequest;
import com.order.api.application.dto.PolicyValidationResponse;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.domain.usecases.ValidateInsurancePolicy;

public record InsurancePolicyInteractor(
        InsurancePolicyGateway insurancePolicyGateway,
        PolicyValidationGateway policyValidationGateway,
        ValidateInsurancePolicy validateInsurancePolicy
) implements CreateInsurancePolicy {

    public InsurancePolicy create(InsurancePolicy insurancePolicy) {
        InsurancePolicy createdInsurancePolicy = insurancePolicyGateway.createInsurancePolicy(insurancePolicy);
        PolicyValidationRequest policyValidationRequest = new PolicyValidationRequest(createdInsurancePolicy.getId(), createdInsurancePolicy.getCustomerId());
        PolicyValidationResponse policyValidationResponse = policyValidationGateway.validate(policyValidationRequest);
        validateInsurancePolicy.validate(insurancePolicy, policyValidationResponse.classification());
        return createdInsurancePolicy;
    }
}
