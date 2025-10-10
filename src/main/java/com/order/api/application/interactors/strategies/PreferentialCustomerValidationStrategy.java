package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.usecases.PolicyValidationStrategy;

import java.math.BigDecimal;

public class PreferentialCustomerValidationStrategy implements PolicyValidationStrategy {
    private final BigDecimal LIFE_LIMIT = BigDecimal.valueOf(800000);
    private static final BigDecimal AUTO_PROPERTY_LIMIT = BigDecimal.valueOf(450000);
    private static final BigDecimal OTHER_LIMIT = BigDecimal.valueOf(375000);

    public boolean isValid(BigDecimal insuredAmount, PolicyCategory category) {
        return switch (category) {
            case LIFE -> insuredAmount.compareTo(LIFE_LIMIT) <= 0;
            case AUTO, PROPERTY -> insuredAmount.compareTo(AUTO_PROPERTY_LIMIT) <= 0;
            default -> insuredAmount.compareTo(OTHER_LIMIT) <= 0;
        };
    }
}
