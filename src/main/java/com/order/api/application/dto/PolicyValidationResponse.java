package com.order.api.application.dto;

import com.order.api.domain.enums.CustomerType;

import java.util.List;
import java.util.UUID;

public record PolicyValidationResponse(
        UUID orderId,
        UUID customerId,
        String analyzedAt,
        CustomerType classification,
        List<Occurrence> occurrences
) {
}
