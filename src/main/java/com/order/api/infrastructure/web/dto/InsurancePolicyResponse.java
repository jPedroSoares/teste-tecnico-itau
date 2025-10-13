package com.order.api.infrastructure.web.dto;

import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record InsurancePolicyResponse(
        UUID id,
        UUID customerId,
        UUID productId,
        PolicyCategory category,
        SalesChannel salesChannel,
        PaymentMethod paymentMethod,
        InsurancePolicyStatus status,
        LocalDateTime createdAt,
        LocalDateTime finishedAt,
        BigDecimal totalMonthlyPremiumAmount,
        BigDecimal insuredAmount,
        Map<String, BigDecimal> coverages,
        List<String> assistances,
        List<HistoryEntry> history
) { }
