package com.order.api.application.dto;

import com.order.api.application.enums.ClassificationType;

import java.util.List;
import java.util.UUID;

public record PolicyValidationResponse(
        UUID orderId,
        UUID customerId,
        String analyzedAt,
        ClassificationType classification,
        List<Occurrence> occurrences
) {
}
