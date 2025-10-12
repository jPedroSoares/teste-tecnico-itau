package com.order.api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PolicyHistoryRepository extends JpaRepository<PolicyHistoryEntity, UUID> {
}
