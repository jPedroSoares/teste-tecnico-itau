package com.order.api.infrastructure.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class HealthCheckController {
    private final DataSource dataSource;
    private final AdminClient kafkaAdminClient;
    @Value("${antifraud.api.url}")
    private String antiFraudApiUrl;

    public HealthCheckController(DataSource dataSource, AdminClient kafkaAdminClient) {
        this.dataSource = dataSource;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();

        CompletableFuture<Boolean> dbFuture = CompletableFuture.supplyAsync(this::checkDatabaseHealth);
        CompletableFuture<Boolean> kafkaFuture = CompletableFuture.supplyAsync(this::checkKafkaHealth);

        try {
            boolean dbHealthy = dbFuture.get(5, TimeUnit.SECONDS);
            boolean kafkaHealthy = kafkaFuture.get(5, TimeUnit.SECONDS);

            health.put("database", dbHealthy ? "UP" : "DOWN");
            health.put("kafka", kafkaHealthy ? "UP" : "DOWN");

            health.put("status", dbHealthy ? "UP" : "DOWN");

        } catch (Exception e) {
            log.error("Health check timeout or error", e);
            health.put("status", "DOWN");
            health.put("error", "Health check timeout");
        }

        health.put("timestamp", LocalDateTime.now());
        health.put("application", "Insurance Policy API");

        return health.containsValue("DOWN")
                ? ResponseEntity.status(503).body(health)
                : ResponseEntity.ok(health);
    }

    private boolean checkDatabaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkKafkaHealth() {
        try {
            DescribeClusterResult clusterResult = kafkaAdminClient.describeCluster();
            clusterResult.clusterId().get(5, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.warn("Kafka health check failed: {}", e.getMessage());
            return false;
        }
    }
}
