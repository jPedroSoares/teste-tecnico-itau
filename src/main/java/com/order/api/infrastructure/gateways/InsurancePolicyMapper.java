package com.order.api.infrastructure.gateways;

import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;

import java.util.List;
import java.util.Optional;

public record InsurancePolicyMapper(HistoryEntryMapper historyEntryMapper) {
    InsurancePolicyEntity toEntity(InsurancePolicy insurancePolicyDomainObj) {
        return new InsurancePolicyEntity(
                insurancePolicyDomainObj.getCustomerId(),
                insurancePolicyDomainObj.getProductId(),
                insurancePolicyDomainObj.getCategory(),
                insurancePolicyDomainObj.getStatus().getStatusName(),
                insurancePolicyDomainObj.getSalesChannel(),
                insurancePolicyDomainObj.getPaymentMethod(),
                insurancePolicyDomainObj.getTotalMonthlyPremiumAmount(),
                insurancePolicyDomainObj.getInsuredAmount(),
                insurancePolicyDomainObj.getCoverages(),
                insurancePolicyDomainObj.getAssistances()
        );
    }

    InsurancePolicy toDomain(InsurancePolicyEntity insurancePolicyEntity) {
        InsurancePolicy insurancePolicy = new InsurancePolicy(
                insurancePolicyEntity.getId(),
                insurancePolicyEntity.getCustomerId(),
                insurancePolicyEntity.getProductId(),
                insurancePolicyEntity.getCategory(),
                insurancePolicyEntity.getCoverages(),
                insurancePolicyEntity.getAssistances(),
                insurancePolicyEntity.getTotalMonthlyPremiumAmount(),
                insurancePolicyEntity.getInsuredAmount(),
                insurancePolicyEntity.getPaymentMethod(),
                insurancePolicyEntity.getSalesChannel()
        );
        List<HistoryEntry> historyEntryDomain = historyEntryMapper.toDomain(insurancePolicyEntity.getHistory());
        insurancePolicy.setHistory(historyEntryDomain);
        return insurancePolicy;
    }

    public InsurancePolicy toDomain(Optional<InsurancePolicyEntity> entity) {
        return entity.map(this::toDomain).orElse(null);
    }

    public List<InsurancePolicy> toDomain(List<InsurancePolicyEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }
}
