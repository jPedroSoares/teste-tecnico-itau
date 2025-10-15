package com.order.api.application.gateways;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;

import java.util.List;
import java.util.UUID;

public interface InsurancePolicyGateway {
    InsurancePolicy createInsurancePolicy(InsurancePolicy insurancePolicy);
    InsurancePolicy findById(UUID id);
    List<InsurancePolicy> findByCustomerId(UUID customerId);
    void cancelInsurancePolicy(UUID id);
    void updateStatus(UUID id, InsurancePolicyStatus status);
    void finishPolicy(InsurancePolicy insurancePolicy);
}
