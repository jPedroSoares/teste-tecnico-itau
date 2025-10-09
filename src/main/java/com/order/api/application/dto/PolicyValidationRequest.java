package com.order.api.application.dto;

import java.util.UUID;

public record PolicyValidationRequest(
        UUID orderId,
        UUID customerId
) {
}
