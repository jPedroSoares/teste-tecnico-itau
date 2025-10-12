package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;

public interface InsurancePolicyState {
    void validate(InsurancePolicy policy);
    void process(InsurancePolicy policy);
    void approve(InsurancePolicy policy);
    void reject(InsurancePolicy policy);
    void cancel(InsurancePolicy policy);
    InsurancePolicyStatus getStatusName();
}
