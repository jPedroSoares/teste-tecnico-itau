package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;

public class Approved implements InsurancePolicyState {
    @Override
    public void validate(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot validate a policy in APPROVED state.");
    }
    @Override
    public void process(InsurancePolicy policy) {
        InsurancePolicyState newStatus = new Validated();
        policy.setStatus(newStatus);
        policy.addHistoryEntry(newStatus);
    }
    @Override
    public void approve(InsurancePolicy policy) {
        throw new IllegalStateException("Policy is already in APPROVED state.");
    }

    @Override
    public void reject(InsurancePolicy policy) {
        throw new IllegalStateException("Cannot reject a policy in APPROVED state.");
    }

    @Override
    public void cancel(InsurancePolicy policy) {
        throw  new IllegalStateException("Cannot cancel a policy in APPROVED state.");
    }

    @Override
    public InsurancePolicyStatus getStatusName() {
        return InsurancePolicyStatus.APPROVED;
    }
}
