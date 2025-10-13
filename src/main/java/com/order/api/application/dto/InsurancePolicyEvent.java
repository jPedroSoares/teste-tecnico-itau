package com.order.api.application.dto;

import com.order.api.application.enums.EventType;
import com.order.api.domain.enums.InsurancePolicyStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record InsurancePolicyEvent (
        UUID eventId,
        EventType eventType,
        UUID policyId,
        UUID customerId,
        InsurancePolicyStatus status,
        String timestamp
        ) {
}
