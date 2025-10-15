package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.exceptions.InvalidPolicyStateException;

public class Validated implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        throw new InvalidPolicyStateException("Policy is already in VALIDATED state.");
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
        throw new InvalidPolicyStateException("reject", getStatusName());
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Canceled();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.VALIDATED;
    }
}
