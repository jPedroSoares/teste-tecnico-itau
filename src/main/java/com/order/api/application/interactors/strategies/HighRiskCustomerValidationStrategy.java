package com.order.api.application.interactors.strategies;

import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.usecases.PolicyValidationStrategy;

import java.math.BigDecimal;

public class HighRiskCustomerValidationStrategy implements PolicyValidationStrategy {
    private static final BigDecimal AUTO_LIMIT = BigDecimal.valueOf(250000);
    private static final BigDecimal PROPERTY_LIMIT = BigDecimal.valueOf(150000);
    private static final BigDecimal OTHER_LIMIT = BigDecimal.valueOf(125000);

    public boolean isValid(BigDecimal insuredAmount, PolicyCategory category) {
        return switch (category) {
            case AUTO -> insuredAmount.compareTo(AUTO_LIMIT) <= 0;
            case PROPERTY -> insuredAmount.compareTo(PROPERTY_LIMIT) <= 0;
            default -> insuredAmount.compareTo(OTHER_LIMIT) <= 0;
        };
    }
}
