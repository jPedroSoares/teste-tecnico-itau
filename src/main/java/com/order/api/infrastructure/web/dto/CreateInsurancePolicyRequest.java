package com.order.api.infrastructure.web.dto;

import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateInsurancePolicyRequest(
        UUID customerId,
        UUID productId,
        PolicyCategory category,
        SalesChannel salesChannel,
        PaymentMethod paymentMethod,
        Double totalMonthlyPremiumAmount,
        Double insuredAmount,
        Map<String, Double> coverages,
        List<String> assistances
) {
}
