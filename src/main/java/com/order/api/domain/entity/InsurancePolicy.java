package com.order.api.domain.entity;

import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class InsurancePolicy
    {
        private UUID id;
        private UUID customerId;
        private UUID productId;
        private PolicyCategory category;
        private Map<String, BigDecimal> coverages;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime finishedAt;
        private List<String> assistances;
        private BigDecimal totalMonthlyPremiumAmount;
        private BigDecimal insuredAmount;
        private PaymentMethod paymentMethod;
        private SalesChannel salesChannel;
        private List<HistoryEntry> history;

    public InsurancePolicy(
            UUID customerId,
            UUID productId,
            PolicyCategory category,
            Map<String, BigDecimal> coverages,
            List<String> assistances,
            BigDecimal totalMonthlyPremiumAmount,
            BigDecimal insuredAmount,
            PaymentMethod paymentMethod,
            SalesChannel salesChannel
    ) {
        this.customerId = customerId;
        this.productId = productId;
        this.category = category;
        this.coverages = coverages;
        this.assistances = assistances;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.insuredAmount = insuredAmount;
        this.paymentMethod = paymentMethod;
        this.salesChannel = salesChannel;
        this.status = "RECEIVED";
        this.createdAt = LocalDateTime.now();
    }
}

