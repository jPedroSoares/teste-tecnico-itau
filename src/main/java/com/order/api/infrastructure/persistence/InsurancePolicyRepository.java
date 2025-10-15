package com.order.api.infrastructure.persistence;

import com.order.api.domain.enums.InsurancePolicyStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicyEntity, UUID> {
    @Override
    @EntityGraph(attributePaths = {"history"})
    Optional<InsurancePolicyEntity> findById(UUID id);

    @EntityGraph(attributePaths = {"history"})
    List<InsurancePolicyEntity> findByCustomerId(UUID customerId);

    @Modifying
    @Transactional
    @Query("UPDATE InsurancePolicyEntity ip SET ip.status = :status WHERE ip.id = :id")
    void updateStatusById(@Param("id") UUID id, @Param("status") InsurancePolicyStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE InsurancePolicyEntity ip SET ip.finishedAt = :finishedAt WHERE ip.id = :id")
    void finish(@Param("id") UUID id, @Param("finishedAt") LocalDateTime finishedAt);
}
