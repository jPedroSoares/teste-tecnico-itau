package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.usecases.PolicyValidationStrategy;

import java.math.BigDecimal;

public class RegularCustomerValidationStrategy implements PolicyValidationStrategy {
    private final BigDecimal LIFE_PROPERTY_LIMIT = BigDecimal.valueOf(500000);
    private static final BigDecimal AUTO_LIMIT = BigDecimal.valueOf(350000);
    private static final BigDecimal OTHER_LIMIT = BigDecimal.valueOf(255000);

    public boolean isValid(BigDecimal insuredAmount, PolicyCategory category) {
        return switch (category) {
            case LIFE, PROPERTY -> insuredAmount.compareTo(LIFE_PROPERTY_LIMIT) <= 0;
            case AUTO -> insuredAmount.compareTo(AUTO_LIMIT) <= 0;
            default -> insuredAmount.compareTo(OTHER_LIMIT) <= 0;
        };
    }
}
