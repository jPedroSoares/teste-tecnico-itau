package com.order.api.domain.entity;

import com.order.api.domain.enums.InsurancePolicyStatus;

import java.time.LocalTime;

public record HistoryEntry(
    LocalTime timestamp,
    InsurancePolicyStatus status
) {
}
