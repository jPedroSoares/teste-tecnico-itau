package com.order.api.application.interactors;

import com.order.api.application.interactors.strategies.PolicyValidationStrategyFactory;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.CustomerType;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.domain.usecases.PolicyValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateInsurancePolicyImpTest {

    @Mock
    private PolicyValidationStrategyFactory strategyFactory;

    @Mock
    private PolicyValidationStrategy mockStrategy;

    private ValidateInsurancePolicyImp validateInsurancePolicy;

    @BeforeEach
    void setUp() {
        validateInsurancePolicy = new ValidateInsurancePolicyImp(strategyFactory);
    }

    private InsurancePolicy buildInsurancePolicy(BigDecimal insuredAmount, PolicyCategory category) {
        return new InsurancePolicy(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                category,
                Map.of("coverage1", BigDecimal.valueOf(1000)),
                List.of("assistance1", "assistance2"),
                BigDecimal.valueOf(100.0),
                null,
                null,
                insuredAmount,
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
    }

    private InsurancePolicy buildDefaultInsurancePolicy() {
        return buildInsurancePolicy(BigDecimal.valueOf(100000), PolicyCategory.AUTO);
    }

    @Test
    @DisplayName("Should delegate to strategy factory and return true when strategy validates successfully")
    void shouldDelegateToStrategyFactoryAndReturnTrueWhenValid() {
        InsurancePolicy policy = buildDefaultInsurancePolicy();
        CustomerType customerType = CustomerType.REGULAR;

        when(strategyFactory.getStrategy(customerType)).thenReturn(mockStrategy);
        when(mockStrategy.isValid(policy.getInsuredAmount(), policy.getCategory())).thenReturn(true);

        boolean result = validateInsurancePolicy.validate(policy, customerType);

        assertTrue(result);
        verify(strategyFactory).getStrategy(customerType);
        verify(mockStrategy).isValid(policy.getInsuredAmount(), policy.getCategory());
    }

    @Test
    @DisplayName("Should delegate to strategy factory and return false when strategy validation fails")
    void shouldDelegateToStrategyFactoryAndReturnFalseWhenInvalid() {
        InsurancePolicy policy = buildDefaultInsurancePolicy();
        CustomerType customerType = CustomerType.HIGH_RISK;

        when(strategyFactory.getStrategy(customerType)).thenReturn(mockStrategy);
        when(mockStrategy.isValid(policy.getInsuredAmount(), policy.getCategory())).thenReturn(false);

        boolean result = validateInsurancePolicy.validate(policy, customerType);

        assertFalse(result);
        verify(strategyFactory).getStrategy(customerType);
        verify(mockStrategy).isValid(policy.getInsuredAmount(), policy.getCategory());
    }

    @Test
    @DisplayName("Should pass correct parameters to strategy")
    void shouldPassCorrectParametersToStrategy() {
        BigDecimal expectedAmount = new BigDecimal("125000.50");
        PolicyCategory expectedCategory = PolicyCategory.PROPERTY;
        CustomerType expectedCustomerType = CustomerType.NO_INFORMATION;
        
        InsurancePolicy policy = buildInsurancePolicy(expectedAmount, expectedCategory);

        when(strategyFactory.getStrategy(expectedCustomerType)).thenReturn(mockStrategy);
        when(mockStrategy.isValid(expectedAmount, expectedCategory)).thenReturn(true);

        boolean result = validateInsurancePolicy.validate(policy, expectedCustomerType);

        assertTrue(result);
        verify(strategyFactory).getStrategy(expectedCustomerType);
        verify(mockStrategy).isValid(expectedAmount, expectedCategory);
    }

    @Test
    @DisplayName("Should propagate exception from strategy factory")
    void shouldPropagateExceptionFromStrategyFactory() {
        InsurancePolicy policy = buildDefaultInsurancePolicy();
        CustomerType customerType = CustomerType.REGULAR;
        IllegalArgumentException expectedException = new IllegalArgumentException("Strategy not found");

        when(strategyFactory.getStrategy(customerType)).thenThrow(expectedException);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            validateInsurancePolicy.validate(policy, customerType);
        });

        assertEquals("Strategy not found", thrownException.getMessage());
        verify(strategyFactory).getStrategy(customerType);
    }

}