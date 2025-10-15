package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.domain.exceptions.InvalidPolicyStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PendingTest {
    private InsurancePolicy insurancePolicy;

    @BeforeEach
    void setUp() {
        insurancePolicy = new InsurancePolicy(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                PolicyCategory.LIFE,
                Map.of("any_coverage", BigDecimal.valueOf(5000)),
                List.of("any_assistance"),
                BigDecimal.valueOf(200000),
                null,
                null,
                BigDecimal.valueOf(1500),
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
        insurancePolicy.setStatus(new Pending());
    }

    @Test
    @DisplayName("Should throw exception when validate is called on Pending state")
    void testValidateThrowsException() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.validate();
        });
        assertEquals("Cannot validate a policy in PENDING state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should call process on Pending state without changing state")
    void testProcessOnPending() {
        insurancePolicy.process();
        assertEquals(InsurancePolicyStatus.PENDING, insurancePolicy.getStatus().getStatusName());
        assertTrue(insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.PENDING));
    }

    @Test
    @DisplayName("Should transition to Approved state when approve is called on Pending state")
    void testApproveOnPending() {
        insurancePolicy.approve();
        assertEquals(InsurancePolicyStatus.APPROVED, insurancePolicy.getStatus().getStatusName());
        assertTrue(insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.APPROVED));
    }

    @Test
    @DisplayName("Should transition to Rejected state when reject is called on Pending state")
    void testRejectOnPending() {
        insurancePolicy.reject();
        assertEquals(InsurancePolicyStatus.REJECTED, insurancePolicy.getStatus().getStatusName());
        assertTrue(insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.REJECTED));
    }

    @Test
    @DisplayName("Should transition to Canceled state when cancel is called on Pending state")
    void testCancelOnPending() {
        insurancePolicy.cancel();
        assertEquals(InsurancePolicyStatus.CANCELED, insurancePolicy.getStatus().getStatusName());
        assertTrue(insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.CANCELED));
    }

    @Test
    @DisplayName("Should return PENDING as status name")
    void testGetStatusName() {
        assertEquals(InsurancePolicyStatus.PENDING, insurancePolicy.getStatus().getStatusName());
    }
}