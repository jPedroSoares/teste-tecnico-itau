package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.CustomerType;
import com.order.api.domain.usecases.PolicyValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class PolicyValidationStrategyFactoryTest {
    private PolicyValidationStrategyFactory factory;
    private RegularCustomerValidationStrategy regularStrategy;
    private HighRiskCustomerValidationStrategy highRiskStrategy;
    private PreferentialCustomerValidationStrategy preferentialStrategy;
    private NoInformationCustomerValidationStrategy noInfoStrategy;

    @BeforeEach
    void setUp() {
        regularStrategy = new RegularCustomerValidationStrategy();
        highRiskStrategy = new HighRiskCustomerValidationStrategy();
        preferentialStrategy = new PreferentialCustomerValidationStrategy();
        noInfoStrategy = new NoInformationCustomerValidationStrategy();

        factory = new PolicyValidationStrategyFactory(
                regularStrategy,
                highRiskStrategy,
                preferentialStrategy,
                noInfoStrategy
        );
    }

    @ParameterizedTest
    @EnumSource(CustomerType.class)
    @DisplayName("should return non-null strategy for all customer types")
    void shouldReturnNonNullStrategyForAllCustomerTypes(CustomerType customerType) {
        PolicyValidationStrategy strategy = factory.getStrategy(customerType);

        assertNotNull(strategy,
                () -> "Strategy should not be null for customer type: " + customerType);
    }

    @Test
    @DisplayName("should return correct strategy instances for each customer type")
    void shouldReturnCorrectStrategyInstances() {
        assertSame(regularStrategy, factory.getStrategy(CustomerType.REGULAR));
        assertSame(highRiskStrategy, factory.getStrategy(CustomerType.HIGH_RISK));
        assertSame(preferentialStrategy, factory.getStrategy(CustomerType.PREFERENTIAL));
        assertSame(noInfoStrategy, factory.getStrategy(CustomerType.NO_INFORMATION));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for null customer type")
    void shouldThrowExceptionForNullCustomerType() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.getStrategy(null)
        );

        assertEquals("Customer type cannot be null", exception.getMessage());
    }
}