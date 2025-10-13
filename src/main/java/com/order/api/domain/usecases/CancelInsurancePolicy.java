package com.order.api.domain.usecases;

import java.util.UUID;

public interface CancelInsurancePolicy {
    void cancelPolicy(UUID policyId);
}
