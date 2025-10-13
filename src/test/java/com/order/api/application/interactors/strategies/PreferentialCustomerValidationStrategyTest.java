package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PreferentialCustomerValidationStrategyTest {

    @Test
    @DisplayName("should return true when insured amount is within limit for LIFE category")
    void testLifeCategoryWithinLimit() {
        PreferentialCustomerValidationStrategy strategy = new PreferentialCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(800000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for LIFE category")
    void testLifeCategoryExceedsLimit() {
        PreferentialCustomerValidationStrategy strategy = new PreferentialCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(800001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
    }

    @Test
    @DisplayName("should return true when insured amount is within limit for AUTO or PROPERTY category")
    void testAutoPropertyCategoryWithinLimit() {
        PreferentialCustomerValidationStrategy strategy = new PreferentialCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(450000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for AUTO or PROPERTY category")
    void testAutoPropertyCategoryExceedsLimit() {
        PreferentialCustomerValidationStrategy strategy = new PreferentialCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(450001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
    }

    @Test
    @DisplayName("should return true when insured amount is within limit for OTHER categories")
    void testOtherCategoryWithinLimit() {
        PreferentialCustomerValidationStrategy strategy = new PreferentialCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(375000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for OTHER categories")
    void testOtherCategoryExceedsLimit() {
        PreferentialCustomerValidationStrategy strategy = new PreferentialCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(375001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }
}