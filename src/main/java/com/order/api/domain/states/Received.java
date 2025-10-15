package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.exceptions.InvalidPolicyStateException;

public class Received implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Validated();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
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
