package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.exceptions.InvalidPolicyStateException;

public class Canceled implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("validate", getStatusName());
    }

    @Override
    public void process(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("process", getStatusName());
    }

    @Override
    public void approve(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("approve", getStatusName());
    }

    @Override
    public void reject(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("reject", getStatusName());
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("Policy is already in CANCELED state.");
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.CANCELED;
    }
}
