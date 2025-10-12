package com.order.api.domain.entity;

import com.order.api.domain.enums.InsurancePolicyStatus;
import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import com.order.api.domain.states.InsurancePolicyState;
import com.order.api.domain.states.Received;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
        private InsurancePolicyState status;
        private LocalDateTime createdAt;
        private LocalDateTime finishedAt;
        private List<String> assistances;
        private BigDecimal totalMonthlyPremiumAmount;
        private BigDecimal insuredAmount;
        private PaymentMethod paymentMethod;
        private SalesChannel salesChannel;
        private List<HistoryEntry> history;

    public InsurancePolicy(
            UUID id,
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
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.category = category;
        this.coverages = coverages;
        this.assistances = assistances;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.insuredAmount = insuredAmount;
        this.paymentMethod = paymentMethod;
        this.salesChannel = salesChannel;
        this.status = new Received();
        this.createdAt = LocalDateTime.now();
        this.history = new ArrayList<>();
        this.addHistoryEntry(this.status);
    }

    public void addHistoryEntry(InsurancePolicyState state) {
        InsurancePolicyStatus status = state.getStatusName();
        HistoryEntry historyEntry = new HistoryEntry(LocalTime.now(), status);
            this.history.add(historyEntry);
    }

    public void validate() {
        this.status.validate(this);
    }

    public void process() {
        this.status.process(this);
    }

    public void approve() {
        this.status.approve(this);
    }

    public void reject() {
        this.status.reject(this);
    }

    public void cancel() {
        this.status.cancel(this);
    }
}
