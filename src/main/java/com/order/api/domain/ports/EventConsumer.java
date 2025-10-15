package com.order.api.domain.ports;

public interface EventConsumer {
    void consume(String message);
}
