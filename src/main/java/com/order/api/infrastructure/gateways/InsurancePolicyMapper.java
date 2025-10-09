package com.order.api.infrastructure.gateways;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.persistence.InsurancePolicyEntity;

public class InsurancePolicyMapper {
    InsurancePolicyEntity toEntity(InsurancePolicy insurancePolicyDomainObj) {
        return new InsurancePolicyEntity(
                insurancePolicyDomainObj.getCustomerId(),
                insurancePolicyDomainObj.getProductId(),
                insurancePolicyDomainObj.getCategory(),
                insurancePolicyDomainObj.getSalesChannel(),
                insurancePolicyDomainObj.getPaymentMethod(),
                insurancePolicyDomainObj.getTotalMonthlyPremiumAmount(),
                insurancePolicyDomainObj.getInsuredAmount(),
                insurancePolicyDomainObj.getCoverages(),
                insurancePolicyDomainObj.getAssistances()
        );
    }

    InsurancePolicy toDomain(InsurancePolicyEntity insurancePolicyEntity) {
        return new InsurancePolicy(
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
    }

}
