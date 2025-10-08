package com.order.api.application.gateways;

import com.order.api.domain.entity.InsurancePolicy;

public interface InsurancePolicyGateway {
    InsurancePolicy createInsurancePolicy(InsurancePolicy insurancePolicy);
}
