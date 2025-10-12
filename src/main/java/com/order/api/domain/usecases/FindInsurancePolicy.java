package com.order.api.domain.usecases;

import com.order.api.domain.entity.InsurancePolicy;

import java.util.UUID;

public interface FindInsurancePolicy {
    InsurancePolicy find(UUID id);
}
