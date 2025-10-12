package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;

public class Received implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Validated();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }

    @Override
    public void process(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot process a policy in RECEIVED state.");
    }

    @Override
    public void approve(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot approve a policy in RECEIVED state.");
    }

    @Override
    public void reject(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Rejected();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Canceled();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.RECEIVED;
    }
}
