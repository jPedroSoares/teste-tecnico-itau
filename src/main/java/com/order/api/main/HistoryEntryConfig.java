package com.order.api.main;

import com.order.api.application.gateways.HistoryEntryGateway;
import com.order.api.infrastructure.gateways.HistoryEntryMapper;
import com.order.api.infrastructure.gateways.HistoryEntryRepositoryGateway;
import com.order.api.infrastructure.gateways.InsurancePolicyMapper;
import com.order.api.infrastructure.persistence.PolicyHistoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoryEntryConfig {
    @Bean
    HistoryEntryGateway historyEntryGateway( HistoryEntryMapper historyEntryMapper,
                                             PolicyHistoryRepository policyHistoryRepository,
                                             InsurancePolicyMapper insurancePolicyMapper){
        return new HistoryEntryRepositoryGateway(historyEntryMapper, policyHistoryRepository, insurancePolicyMapper);
    }

    @Bean
    HistoryEntryMapper historyEntryMapper(){
        return new HistoryEntryMapper();
    }
}
