package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HighRiskCustomerValidationStrategyTest {

    @Test
    @DisplayName("Should return false when insured amount exceeds limit for AUTO category")
    void AutoInvalid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = new BigDecimal("250001");
        boolean isValid = strategy.isValid(insuredAmount, PolicyCategory.AUTO);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true when insured amount is within limit for AUTO category")
    void AutoValid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = new BigDecimal("250000");
        boolean isValid = strategy.isValid(insuredAmount, PolicyCategory.AUTO);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when insured amount exceeds limit for PROPERTY category")
    void PropertyInvalid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = new BigDecimal("150001");
        boolean isValid = strategy.isValid(insuredAmount, PolicyCategory.PROPERTY);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true when insured amount is within limit for PROPERTY category")
    void PropertyValid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = new BigDecimal("150000");
        boolean isValid = strategy.isValid(insuredAmount, PolicyCategory.PROPERTY);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when insured amount exceeds limit for OTHER category")
    void OtherInvalid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = new BigDecimal("125001");
        boolean isValid = strategy.isValid(insuredAmount, PolicyCategory.LIFE);
        assertFalse(isValid);
        isValid = strategy.isValid(insuredAmount, PolicyCategory.BUSINESS);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true when insured amount is within limit for OTHER category")
    void OtherValid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = new BigDecimal("125000");
        boolean isValid = strategy.isValid(insuredAmount, PolicyCategory.LIFE);
        assertTrue(isValid);
        isValid = strategy.isValid(insuredAmount, PolicyCategory.BUSINESS);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return true when insured amount is exactly zero")
    void ZeroAmountValid() {
        HighRiskCustomerValidationStrategy strategy = new HighRiskCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.ZERO;
        boolean isValidAuto = strategy.isValid(insuredAmount, PolicyCategory.AUTO);
        boolean isValidProperty = strategy.isValid(insuredAmount, PolicyCategory.PROPERTY);
        boolean isValidOther = strategy.isValid(insuredAmount, PolicyCategory.LIFE);
        assertTrue(isValidAuto);
        assertTrue(isValidProperty);
        assertTrue(isValidOther);
    }
}