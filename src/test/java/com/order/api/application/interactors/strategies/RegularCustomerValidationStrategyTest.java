package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RegularCustomerValidationStrategyTest {

    @Test
    @DisplayName("should return true when insured amount is within limit for LIFE or PROPERTY category")
    void testLifePropertyCategoryWithinLimit() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(500000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for LIFE or PROPERTY category")
    void testLifePropertyCategoryExceedsLimit() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(500001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
    }

    @Test
    @DisplayName("should return true when insured amount is within limit for AUTO category")
    void testAutoCategoryWithinLimit() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(350000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for AUTO category")
    void testAutoCategoryExceedsLimit() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(350001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
    }

    @Test
    @DisplayName("should return true when insured amount is within limit for OTHER category")
    void testOtherCategoryWithinLimit() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(255000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for OTHER category")
    void testOtherCategoryExceedsLimit() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(255001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }

    @Test
    @DisplayName("should return true when insured amount is zero for any category")
    void testZeroInsuredAmount() {
        RegularCustomerValidationStrategy strategy = new RegularCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.ZERO;
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }
}