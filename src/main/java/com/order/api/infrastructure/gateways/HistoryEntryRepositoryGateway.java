package com.order.api.infrastructure.gateways;

import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;
import com.order.api.infrastructure.persistence.PolicyHistoryEntity;
import com.order.api.infrastructure.persistence.PolicyHistoryRepository;

public class HistoryEntryRepositoryGateway implements HistoryEntryGateway {
    private final HistoryEntryMapper historyEntryMapper;
    private final InsurancePolicyMapper insurancePolicyMapper;
    private final PolicyHistoryRepository policyHistoryRepository;

    public HistoryEntryRepositoryGateway(HistoryEntryMapper historyEntryMapper, PolicyHistoryRepository policyHistoryRepository, InsurancePolicyMapper insurancePolicyMapper) {
        this.historyEntryMapper = historyEntryMapper;
        this.policyHistoryRepository = policyHistoryRepository;
        this.insurancePolicyMapper = insurancePolicyMapper;

    }

    public HistoryEntry create(HistoryEntry historyEntry, InsurancePolicy insurancePolicy) {
        InsurancePolicyEntity insurancePolicyEntity = insurancePolicyMapper.toEntity(insurancePolicy);
        insurancePolicyEntity.setId(insurancePolicy.getId());
        PolicyHistoryEntity policyHistoryEntity = historyEntryMapper.toEntity(historyEntry, insurancePolicyEntity);
        PolicyHistoryEntity savedPolicyHistory = policyHistoryRepository.save(policyHistoryEntity);
        return historyEntryMapper.toDomain(savedPolicyHistory);
    }
}
