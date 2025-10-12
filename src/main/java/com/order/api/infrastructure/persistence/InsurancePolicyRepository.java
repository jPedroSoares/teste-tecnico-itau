package com.order.api.infrastructure.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicyEntity, UUID> {
    @Override
    @EntityGraph(attributePaths = {"history"})
    Optional<InsurancePolicyEntity> findById(UUID id);
}
