package com.order.api.infrastructure.messaging;

import com.order.api.application.dto.InsurancePolicyEvent;

public interface EventPublisher {
    void publish(InsurancePolicyEvent event);
}
