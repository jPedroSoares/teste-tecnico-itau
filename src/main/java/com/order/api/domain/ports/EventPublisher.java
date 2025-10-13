package com.order.api.domain.ports;

import com.order.api.application.dto.InsurancePolicyEvent;

public interface
EventPublisher {
    void publish(InsurancePolicyEvent event);
}
