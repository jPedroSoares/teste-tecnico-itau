package com.order.api.infrastructure.persistence;

import com.order.api.domain.enums.PaymentMethod;
import com.order.api.domain.enums.PolicyCategory;
import com.order.api.domain.enums.SalesChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "insurance_policy")
public class InsurancePolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    private PolicyCategory category;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Double> coverages;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> assistances;

    private Double totalMonthlyPremiumAmount;

    private Double insuredAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;

    public InsurancePolicyEntity(
            UUID customerId,
            UUID productId,
            PolicyCategory category,
            SalesChannel salesChannel,
            PaymentMethod paymentMethod,
            Double totalMonthlyPremiumAmount,
            Double insuredAmount,
            Map<String, Double> coverages,
            List<String> assistances
    ) {
        this.customerId = customerId;
        this.productId = productId;
        this.category = category;
        this.salesChannel = salesChannel;
        this.paymentMethod = paymentMethod;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.insuredAmount = insuredAmount;
        this.coverages = coverages;
        this.assistances = assistances;
        this.createdAt = LocalDateTime.now();
    }
}
