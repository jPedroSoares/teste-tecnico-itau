package com.order.api.infrastructure.gateways;

import com.order.api.domain.entity.HistoryEntry;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;
import com.order.api.infrastructure.persistence.PolicyHistoryEntity;

public class HistoryEntryMapper {
    PolicyHistoryEntity toEntity(HistoryEntry historyEntryDomainObj, InsurancePolicyEntity insurancePolicy) {
        return new PolicyHistoryEntity(
                historyEntryDomainObj.status(),
                historyEntryDomainObj.timestamp(),
                insurancePolicy

        );
    }

    HistoryEntry toDomain(PolicyHistoryEntity policyHistoryEntity) {
        return new HistoryEntry(
                policyHistoryEntity.getTimestamp(),
                policyHistoryEntity.getStatus()
        );
    }
}
