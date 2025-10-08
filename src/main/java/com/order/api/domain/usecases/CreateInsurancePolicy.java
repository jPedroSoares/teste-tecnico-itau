package com.order.api.domain.usecases;

import com.order.api.domain.entity.InsurancePolicy;

public interface CreateInsurancePolicy {
    InsurancePolicy create(InsurancePolicy insurancePolicy);
}
