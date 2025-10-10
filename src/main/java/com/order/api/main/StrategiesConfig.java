package com.order.api.main;

import com.order.api.application.interactors.strategies.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategiesConfig {
    @Bean
    PolicyValidationStrategyFactory policyValidationStrategyFactory(
            RegularCustomerValidationStrategy regularStrategy,
            HighRiskCustomerValidationStrategy highRiskStrategy,
            PreferentialCustomerValidationStrategy preferentialStrategy,
            NoInformationCustomerValidationStrategy noInfoStrategy
    ) {
        return new PolicyValidationStrategyFactory(
                regularStrategy, highRiskStrategy, preferentialStrategy, noInfoStrategy);
    }

    @Bean
    RegularCustomerValidationStrategy regularCustomerValidationStrategy() {
        return new RegularCustomerValidationStrategy();
    }

    @Bean
    HighRiskCustomerValidationStrategy highRiskCustomerValidationStrategy() {
        return new HighRiskCustomerValidationStrategy();
    }

    @Bean
    PreferentialCustomerValidationStrategy preferentialCustomerValidationStrategy() {
        return new PreferentialCustomerValidationStrategy();
    }

    @Bean
    NoInformationCustomerValidationStrategy noInformationCustomerValidationStrategy() {
        return new NoInformationCustomerValidationStrategy();
    }
}
