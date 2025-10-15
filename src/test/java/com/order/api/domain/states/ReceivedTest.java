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

class ReceivedTest {

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
                BigDecimal.valueOf(1500),
                PaymentMethod.PIX,
                SalesChannel.MOBILE
        );
        insurancePolicy.setStatus(new Received());
    }

    @Test
    @DisplayName("Should transition to Validated state when validate is called on Received state")
    void testValidateOnReceived() {
        insurancePolicy.validate();
        assert insurancePolicy.getStatus().getStatusName() == InsurancePolicyStatus.VALIDATED;
        assert insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.VALIDATED);
    }

    @Test
    @DisplayName("Should throw exception when process is called on Received state")
    void testProcessThrowsException() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.process();
        });
        assertEquals("Cannot process a policy in RECEIVED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when approve is called on Received state")
    void testApproveThrowsException() {
        InvalidPolicyStateException exception = assertThrows(InvalidPolicyStateException.class, () -> {
            insurancePolicy.approve();
        });
        assertEquals("Cannot approve a policy in RECEIVED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should transition to Rejected state when reject is called on Received state")
    void testRejectOnReceived() {
        insurancePolicy.reject();
        assert insurancePolicy.getStatus().getStatusName() == InsurancePolicyStatus.REJECTED;
        assert insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.REJECTED);
    }

    @Test
    @DisplayName("Should transition to Canceled state when cancel is called on Received state")
    void testCancelOnReceived() {
        insurancePolicy.cancel();
        assert insurancePolicy.getStatus().getStatusName() == InsurancePolicyStatus.CANCELED;
        assert insurancePolicy.getHistory().stream()
                .anyMatch(state -> state.status() == InsurancePolicyStatus.CANCELED);
    }

    @Test
    @DisplayName("Should return RECEIVED as status name")
    void testGetStatusName() {
        assertEquals(InsurancePolicyStatus.RECEIVED, insurancePolicy.getStatus().getStatusName());
    }
}