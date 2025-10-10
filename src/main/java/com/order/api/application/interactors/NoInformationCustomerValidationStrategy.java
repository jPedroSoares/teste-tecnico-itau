package com.order.api.application.interactors;

import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.usecases.PolicyValidationStrategy;

import java.math.BigDecimal;

public class NoInformationCustomerValidationStrategy implements PolicyValidationStrategy {
    private static final BigDecimal LIFE_PROPERTY_LIMIT = BigDecimal.valueOf(200000);
    private static final BigDecimal AUTO_LIMIT = BigDecimal.valueOf(75000);
    private static final BigDecimal OTHER_LIMIT = BigDecimal.valueOf(55000);

    public boolean isValid(BigDecimal insuredAmount, PolicyCategory category) {
        return switch (category) {
            case LIFE, PROPERTY -> insuredAmount.compareTo(LIFE_PROPERTY_LIMIT) <= 0;
            case AUTO -> insuredAmount.compareTo(AUTO_LIMIT) <= 0;
            default -> insuredAmount.compareTo(OTHER_LIMIT) <= 0;
        };
    }
}
