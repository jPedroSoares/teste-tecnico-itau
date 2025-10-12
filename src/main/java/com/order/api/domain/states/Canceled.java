package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;

public class Canceled implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot validate a policy in CANCELED state.");
    }

    @Override
    public void process(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot process a policy in CANCELED state.");
    }

    @Override
    public void approve(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot approve a policy in CANCELED state.");
    }

    @Override
    public void reject(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot reject a policy in CANCELED state.");
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        throw new IllegalStateException("Policy is already in CANCELED state.");
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.CANCELED;
    }
}
