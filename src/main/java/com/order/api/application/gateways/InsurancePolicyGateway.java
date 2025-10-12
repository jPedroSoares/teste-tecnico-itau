package com.order.api.application.gateways;

import com.order.api.domain.entity.InsurancePolicy;

import java.util.UUID;

public interface InsurancePolicyGateway {
    InsurancePolicy createInsurancePolicy(InsurancePolicy insurancePolicy);
    InsurancePolicy findById(UUID id);
}
