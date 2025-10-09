package com.order.api.application.dto;

import com.order.api.application.enums.OccurrenceType;

import java.util.UUID;

public record Occurrence(UUID id,
                         Long productId,
                         OccurrenceType type,
                         String description,
                         String createdAt,
                         String updatedAt) {
}