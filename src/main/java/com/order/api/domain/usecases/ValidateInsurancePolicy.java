package com.order.api.domain.usecases;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.CustomerType;

public interface ValidateInsurancePolicy {
    boolean validate(InsurancePolicy policy, CustomerType customerType);
}
