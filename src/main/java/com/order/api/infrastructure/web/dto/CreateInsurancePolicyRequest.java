package com.order.api.infrastructure.web.dto;

import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateInsurancePolicyRequest(
        @NotNull UUID customerId,
        @NotNull UUID productId,
        @NotNull PolicyCategory category,
        @NotNull SalesChannel salesChannel,
        @NotNull PaymentMethod paymentMethod,
        @NotNull @DecimalMin("0.01") BigDecimal totalMonthlyPremiumAmount,
        @NotNull @DecimalMin("0.01") BigDecimal insuredAmount,
        @NotEmpty Map<String, BigDecimal> coverages,
        @NotEmpty List<String> assistances
) {
}
