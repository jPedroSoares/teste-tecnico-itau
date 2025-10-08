package com.order.api.application.interactors;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyInteractorTest {

    @Mock
    InsurancePolicyGateway insurancePolicyGateway;

    @InjectMocks
    private InsurancePolicyInteractor insurancePolicyInteractor;

    @BeforeEach
    void setUp() {
    }

    private InsurancePolicy buildInsurancePolicy() {
        return new InsurancePolicy(
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.PROPERTY,
                Map.of("any_coverage", 5000.0),
                List.of("any_assistance"),
                50.0,
                50000.0,
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
    }

    @Test
    @DisplayName("Should create an insurance policy successfully")
    void createInsurancePolicyCase1() {
        InsurancePolicy insurancePolicy = buildInsurancePolicy();
        InsurancePolicy result = buildInsurancePolicy();

        when(insurancePolicyGateway.createInsurancePolicy(insurancePolicy))
                .thenReturn(result);
        InsurancePolicy response = insurancePolicyInteractor.create(insurancePolicy);

        assertNotNull(response);
        assertEquals(result, response);
    }

    @Test
    @DisplayName("Should throw if insurancePolicyGateway throws")
    void createInsurancePolicyCase2() {
        InsurancePolicy insurancePolicy = buildInsurancePolicy();

        when(insurancePolicyGateway.createInsurancePolicy(insurancePolicy)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> insurancePolicyInteractor.create(insurancePolicy));
    }
}