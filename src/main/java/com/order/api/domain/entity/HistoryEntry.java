package com.order.api.domain.entity;

import com.order.api.domain.enums.InsurancePolicyStatus;

import java.time.LocalDateTime;

public record HistoryEntry(
    LocalDateTime timestamp,
    InsurancePolicyStatus status
) {
}
