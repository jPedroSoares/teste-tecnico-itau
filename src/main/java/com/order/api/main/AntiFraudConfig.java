package com.order.api.main;

import com.order.api.application.gateways.PolicyValidationGateway;
import com.order.api.infrastructure.gateways.PolicyValidationGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AntiFraudConfig {
    @Bean
    RestClient antiFraudClient(@Value("${antifraud.api.url}") String antiFraudUrl) {
        return RestClient.create(antiFraudUrl);
    }

    @Bean
    PolicyValidationGateway policyValidationGateway(RestClient restClient) {
        return new PolicyValidationGatewayImpl(restClient);
    }
}
