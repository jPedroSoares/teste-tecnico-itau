package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.exceptions.InvalidPolicyStateException;

public class Rejected implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        throw  new InvalidPolicyStateException("validate", getStatusName());
    }

    @Override
    public void process(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Pending();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }

    @Override
    public void approve(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("approve", getStatusName());
    }

    @Override
    public void reject(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("Policy is already in REJECTED state.");
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("cancel", getStatusName());
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.REJECTED;
    }
}
