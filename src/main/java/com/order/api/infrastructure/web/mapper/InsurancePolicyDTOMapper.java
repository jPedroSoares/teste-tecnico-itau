package com.order.api.infrastructure.web.mapper;

import com.order.api.domain.entity.InsurancePolicy;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyRequest;
import com.order.api.infrastructure.web.dto.CreateInsurancePolicyResponse;

import java.util.List;

public class InsurancePolicyDTOMapper {
    public CreateInsurancePolicyResponse toResponse (InsurancePolicy insurancePolicy) {
        return new CreateInsurancePolicyResponse(
                insurancePolicy.getId(),
                insurancePolicy.getCustomerId(),
                insurancePolicy.getProductId(),
                insurancePolicy.getCategory(),
                insurancePolicy.getSalesChannel(),
                insurancePolicy.getPaymentMethod(),
                insurancePolicy.getStatus().getStatusName(),
                insurancePolicy.getCreatedAt(),
                insurancePolicy.getFinishedAt(),
                insurancePolicy.getTotalMonthlyPremiumAmount(),
                insurancePolicy.getInsuredAmount(),
                insurancePolicy.getCoverages(),
                insurancePolicy.getAssistances(),
                insurancePolicy.getHistory()
        );
    }

     public InsurancePolicy toInsurancePolicy (CreateInsurancePolicyRequest request){
        return new InsurancePolicy(
                null,
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

    public List<CreateInsurancePolicyResponse> toResponse(List<InsurancePolicy> response) {
        if (response.isEmpty()) {
            return null;
        }
        return response.stream().map(this::toResponse).toList();
    }
}
