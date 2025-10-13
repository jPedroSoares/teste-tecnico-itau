package com.order.api.main;

import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.application.gateways.InsurancePolicyGateway;
import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.application.interactors.CancelInsurancePolicyInteractor;
import com.order.api.application.interactors.FindInsurancePolicyInteractor;
import com.order.api.application.interactors.InsurancePolicyInteractor;
import com.order.api.application.interactors.ValidateInsurancePolicyImp;
import com.order.api.application.interactors.strategies.PolicyValidationStrategyFactory;
import com.order.api.domain.usecases.CancelInsurancePolicy;
import com.order.api.domain.usecases.CreateInsurancePolicy;
import com.order.api.domain.usecases.FindInsurancePolicy;
import com.order.api.domain.usecases.ValidateInsurancePolicy;
import com.order.api.infrastructure.gateways.HistoryEntryMapper;
import com.order.api.infrastructure.gateways.InsurancePolicyMapper;
import com.order.api.infrastructure.gateways.InsurancePolicyRepositoryGateway;
import com.order.api.domain.ports.EventPublisher;
import com.order.api.infrastructure.messaging.KafkaProducer;
import com.order.api.infrastructure.persistence.InsurancePolicyRepository;
import com.order.api.infrastructure.web.mapper.InsurancePolicyDTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsurancePolicyConfig {
    @Bean
    CreateInsurancePolicy createUsecase(InsurancePolicyGateway insurancePolicyGateway, PolicyValidationGateway
            policyValidationGateway, ValidateInsurancePolicy validateInsurancePolicy, KafkaProducer kafkaProducer,
                                        HistoryEntryGateway historyEntryGateway) {
        return new InsurancePolicyInteractor(insurancePolicyGateway, policyValidationGateway, validateInsurancePolicy, kafkaProducer, historyEntryGateway);
    }

    @Bean
    InsurancePolicyGateway insurancePolicyGateway(InsurancePolicyMapper insurancePolicyMapper, InsurancePolicyRepository insurancePolicyRepository) {
        return new InsurancePolicyRepositoryGateway(insurancePolicyMapper, insurancePolicyRepository);
    }

    @Bean
    ValidateInsurancePolicy validateInsurancePolicy(PolicyValidationStrategyFactory strategyFactory) {
        return new ValidateInsurancePolicyImp(strategyFactory);
    }

    @Bean
    InsurancePolicyMapper InsurancePolicyMapper(HistoryEntryMapper historyEntryMapper) {
        return new InsurancePolicyMapper(historyEntryMapper);
    }

    @Bean
    InsurancePolicyDTOMapper insurancePolicyDTOMapper() {
        return new InsurancePolicyDTOMapper();
    }

    @Bean
    FindInsurancePolicy findUsecase(InsurancePolicyGateway insurancePolicyGateway) {
        return new FindInsurancePolicyInteractor(insurancePolicyGateway);
    }

    @Bean
    CancelInsurancePolicy cancelUsecase(InsurancePolicyGateway insurancePolicyGateway,
                                        EventPublisher eventPublisher,
                                        HistoryEntryGateway historyEntryGateway) {
        return new CancelInsurancePolicyInteractor(insurancePolicyGateway, eventPublisher, historyEntryGateway);
    }
}
