package com.order.api.domain.usecases;

import com.order.api.domain.enums.PolicyCategory;

import java.math.BigDecimal;

public interface PolicyValidationStrategy {
    boolean isValid(BigDecimal insuredAmount, PolicyCategory category);
}
