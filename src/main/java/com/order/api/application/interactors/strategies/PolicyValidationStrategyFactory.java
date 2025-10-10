package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.CustomerType;
import com.order.api.domain.usecases.PolicyValidationStrategy;

import java.util.Map;

public class PolicyValidationStrategyFactory {
    private final Map<CustomerType, PolicyValidationStrategy> strategies;

    public PolicyValidationStrategyFactory(
            RegularCustomerValidationStrategy regularStrategy,
            HighRiskCustomerValidationStrategy highRiskStrategy,
            PreferentialCustomerValidationStrategy preferentialStrategy,
            NoInformationCustomerValidationStrategy noInfoStrategy) {

        this.strategies = Map.of(
                CustomerType.REGULAR, regularStrategy,
                CustomerType.HIGH_RISK, highRiskStrategy,
                CustomerType.PREFERENTIAL, preferentialStrategy,
                CustomerType.NO_INFORMATION, noInfoStrategy
        );
    }

    public PolicyValidationStrategy getStrategy(CustomerType customerType) {
        PolicyValidationStrategy strategy = strategies.get(customerType);
        if (strategy == null) {
            throw new IllegalArgumentException("No validation strategy found for customer type: " + customerType);
        }
        return strategy;
    }
}
