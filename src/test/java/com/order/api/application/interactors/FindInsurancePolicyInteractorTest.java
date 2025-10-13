package com.order.api.application.interactors;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FindInsurancePolicyInteractorTest {
    @Mock
    private InsurancePolicyGateway insurancePolicyGateway;

    private FindInsurancePolicyInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new FindInsurancePolicyInteractor(insurancePolicyGateway);
    }

    @Test
    @DisplayName("Should find insurance policy by id")
    void shouldFindInsurancePolicyById() {
        UUID id = UUID.randomUUID();
        InsurancePolicy expectedPolicy = mock(InsurancePolicy.class);
        when(insurancePolicyGateway.findById(id)).thenReturn(expectedPolicy);

        InsurancePolicy result = interactor.find(id);

        assertEquals(expectedPolicy, result);
        verify(insurancePolicyGateway).findById(id);
    }

    @Test
    @DisplayName("Should find insurance policies by customer id")
    void shouldFindInsurancePoliciesByCustomerId() {
        UUID customerId = UUID.randomUUID();
        List<InsurancePolicy> expectedPolicies = Arrays.asList(
                mock(InsurancePolicy.class),
                mock(InsurancePolicy.class)
        );
        when(insurancePolicyGateway.findByCustomerId(customerId)).thenReturn(expectedPolicies);

        List<InsurancePolicy> result = interactor.findByCustomerId(customerId);

        assertEquals(expectedPolicies, result);
        verify(insurancePolicyGateway).findByCustomerId(customerId);
    }

    @Test
    @DisplayName("Should return null when policy not found by id")
    void shouldReturnNullWhenPolicyNotFoundById() {
        UUID id = UUID.randomUUID();
        when(insurancePolicyGateway.findById(id)).thenReturn(null);

        InsurancePolicy result = interactor.find(id);

        assertNull(result);
        verify(insurancePolicyGateway).findById(id);
    }

    @Test
    @DisplayName("Should return empty list when no policies found by customer id")
    void shouldReturnEmptyListWhenNoPoliciesFoundByCustomerId() {
        UUID customerId = UUID.randomUUID();
        List<InsurancePolicy> emptyList = List.of();
        when(insurancePolicyGateway.findByCustomerId(customerId)).thenReturn(emptyList);

        List<InsurancePolicy> result = interactor.findByCustomerId(customerId);

        assertTrue(result.isEmpty());
        verify(insurancePolicyGateway).findByCustomerId(customerId);
    }
}