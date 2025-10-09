package com.order.api.main;

import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.interactors.InsurancePolicyInteractor;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.infrastructure.gateways.InsurancePolicyMapper;
import com.order.api.infrastructure.gateways.InsurancePolicyRepositoryGateway;
import com.order.api.infrastructure.persistence.InsurancePolicyRepository;
import com.order.api.infrastructure.web.mapper.InsurancePolicyDTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsurancePolicyConfig {
    @Bean
    CreateInsurancePolicy createUsecase(InsurancePolicyGateway insurancePolicyGateway) {
        return new InsurancePolicyInteractor(insurancePolicyGateway);
    }

    @Bean
    InsurancePolicyGateway insurancePolicyGateway(InsurancePolicyMapper insurancePolicyMapper, InsurancePolicyRepository insurancePolicyRepository) {
        return new InsurancePolicyRepositoryGateway(insurancePolicyMapper, insurancePolicyRepository);
    }

    @Bean
    InsurancePolicyMapper InsurancePolicyMapper() {
        return new InsurancePolicyMapper();
    }

    @Bean
    InsurancePolicyDTOMapper insurancePolicyDTOMapper() {
        return new InsurancePolicyDTOMapper();
    }
}
