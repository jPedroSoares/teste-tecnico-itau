package com.order.api.domain.entity;

import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record InsurancePolicy(
        UUID customerId,
        UUID productId,
        PolicyCategory category,
        Map<String, Double> coverages,
        List<String> assistances,
        Double totalMonthlyPremiumAmount,
        Double insuredAmount,
        PaymentMethod paymentMethod,
        SalesChannel salesChannel
) {
}
