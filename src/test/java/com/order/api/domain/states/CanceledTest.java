package com.order.api.domain.states;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CanceledTest {
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
        insurancePolicy.setStatus(new Canceled());
    }

    @Test
    @DisplayName("Should throw exception when validate is called on Canceled state")
    void testValidateThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            insurancePolicy.validate();
        });
        assertEquals("Cannot validate a policy in CANCELED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when process is called on Canceled state")
    void testProcessThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            insurancePolicy.process();
        });
        assertEquals("Cannot process a policy in CANCELED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when approve is called on Canceled state")
    void testApproveThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            insurancePolicy.approve();
        });
        assertEquals("Cannot approve a policy in CANCELED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when reject is called on Canceled state")
    void testRejectThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            insurancePolicy.reject();
        });
        assertEquals("Cannot reject a policy in CANCELED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cancel is called on Canceled state")
    void testCancelThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            insurancePolicy.cancel();
        });
        assertEquals("Policy is already in CANCELED state.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return CANCELED status name")
    void testGetStatusName() {
        assertEquals(InsurancePolicyStatus.CANCELED, insurancePolicy.getStatus().getStatusName());
    }
}