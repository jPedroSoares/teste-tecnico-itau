package com.order.api.application.interactors;

import com.order.api.application.interactors.strategies.PolicyValidationStrategyFactory;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.CustomerType;
import com.order.api.domain.usecases.PolicyValidationStrategy;
import com.order.api.domain.usecases.ValidateInsurancePolicy;

public class ValidateInsurancePolicyImp implements ValidateInsurancePolicy {
    private final PolicyValidationStrategyFactory strategyFactory;

    public ValidateInsurancePolicyImp(PolicyValidationStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public boolean validate(InsurancePolicy policy, CustomerType customerType) {
        PolicyValidationStrategy strategy = strategyFactory.getStrategy(customerType);
        return strategy.isValid(policy.getInsuredAmount(), policy.getCategory());
    }
}
