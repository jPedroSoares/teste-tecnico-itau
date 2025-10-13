package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NoInformationCustomerValidationStrategyTest {

    @Test
    @DisplayName("should return true when insured amount is within limit for LIFE or PROPERTY category")
    void testLifeCategoryWithinLimit() {
        NoInformationCustomerValidationStrategy strategy = new NoInformationCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(200000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for LIFE or PROPERTY category")
    void testLifeCategoryExceedsLimit() {
        NoInformationCustomerValidationStrategy strategy = new NoInformationCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(200001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.LIFE));
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.PROPERTY));
    }

    @Test
    @DisplayName("should return true when insured amount is within limit for AUTO category")
    void testAutoCategoryWithinLimit() {
        NoInformationCustomerValidationStrategy strategy = new NoInformationCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(75000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for AUTO category")
    void testAutoCategoryExceedsLimit() {
        NoInformationCustomerValidationStrategy strategy = new NoInformationCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(75001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.AUTO));
    }

    @Test
    @DisplayName("should return true when insured amount is within limit for OTHER category")
    void testOtherCategoryWithinLimit() {
        NoInformationCustomerValidationStrategy strategy = new NoInformationCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(55000);
        assertTrue(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }

    @Test
    @DisplayName("should return false when insured amount exceeds limit for OTHER category")
    void testOtherCategoryExceedsLimit() {
        NoInformationCustomerValidationStrategy strategy = new NoInformationCustomerValidationStrategy();
        BigDecimal insuredAmount = BigDecimal.valueOf(55001);
        assertFalse(strategy.isValid(insuredAmount, PolicyCategory.BUSINESS));
    }
}