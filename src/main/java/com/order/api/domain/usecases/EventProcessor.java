package com.order.api.domain.usecases;

public interface EventProcessor {
    void processEvent(String message);
}
