package com.order.api.application.gateways;

import com.order.api.domain.entity.HistoryEntry;
import com.order.api.domain.entity.InsurancePolicy;

public interface HistoryEntryGateway {
    HistoryEntry create(HistoryEntry historyEntry, InsurancePolicy insurancePolicy);
}
