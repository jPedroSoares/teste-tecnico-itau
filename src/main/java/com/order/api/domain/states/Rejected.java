package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;

public class Rejected implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot validate a policy in REJECTED state.");
    }

    @Override
    public void process(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Pending();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }

    @Override
    public void approve(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot approve a policy in REJECTED state.");
    }

    @Override
    public void reject(InsurancePolicy policy) {
        throw new IllegalStateException("Policy is already in REJECTED state.");
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot cancel a policy in REJECTED state.");
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.REJECTED;
    }
}
