package com.order.api.infrastructure.persistence;

import com.order.api.domain.enums.InsurancePolicyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "policy_history")
@NoArgsConstructor
public class PolicyHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InsurancePolicyStatus status;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_policy_id", nullable = false)
    private InsurancePolicyEntity insurancePolicy;

    public PolicyHistoryEntity(InsurancePolicyStatus status, LocalDateTime timestamp, InsurancePolicyEntity insurancePolicy) {
        this.status = status;
        this.timestamp = timestamp;
        this.insurancePolicy = insurancePolicy;
    }
}
