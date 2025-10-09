package com.order.api.infrastructure.web.mapper;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyResponse;

public class InsurancePolicyDTOMapper {
    public CreateInsurancePolicyResponse toResponse (InsurancePolicy insurancePolicy) {
        return new CreateInsurancePolicyResponse(
                insurancePolicy.getId(),
                insurancePolicy.getCustomerId(),
                insurancePolicy.getProductId(),
                insurancePolicy.getCategory(),
                insurancePolicy.getSalesChannel(),
                insurancePolicy.getPaymentMethod(),
                insurancePolicy.getStatus(),
                insurancePolicy.getCreatedAt(),
                insurancePolicy.getFinishedAt(),
                insurancePolicy.getTotalMonthlyPremiumAmount(),
                insurancePolicy.getInsuredAmount(),
                insurancePolicy.getCoverages(),
                insurancePolicy.getAssistances()
        );
    }

     public InsurancePolicy toInsurancePolicy (CreateInsurancePolicyRequest request){
        return new InsurancePolicy(
                request.customerId(),
                request.productId(),
                request.category(),
                request.coverages(),
                request.assistances(),
                request.totalMonthlyPremiumAmount(),
                request.insuredAmount(),
                request.paymentMethod(),
                request.salesChannel()
        );
     }
}
