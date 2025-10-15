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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RejectedTest {
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
        insurancePolicy.setStatus(new Rejected());
    }

    @Test
    @DisplayName("Should throw exception when validate is called on Rejected state")
    void testValidateOnRejected() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.validate();
        });
        assertEquals("Cannot validate a policy in REJECTED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should transition to Pending state when process is called on Rejected state")
    void testProcessOnRejected() {
        insurancePolicy.process();
        assert insurancePolicy.getStatus().getStatusName() == InsurancePolicyStatus.PENDING;
        assert insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.PENDING);
    }

    @Test
    @DisplayName("Should throw exception when approve is called on Rejected state")
    void testApproveThrowsException() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.approve();
        });
        assertEquals("Cannot approve a policy in REJECTED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when reject is called on Rejected state")
    void testRejectThrowsException() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.reject();
        });
        assertEquals("Policy is already in REJECTED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cancel is called on Rejected state")
    void testCancelThrowsException() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.cancel();
        });
        assertEquals("Cannot cancel a policy in REJECTED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return REJECTED status name")
    void testGetStatusName() {
        assertEquals(InsurancePolicyStatus.REJECTED, insurancePolicy.getStatus().getStatusName());
    }

}