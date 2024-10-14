package com.iot.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DatabaseService {

    // Simulated database connection check
    public Mono<String> testConnection() {
        // Example logic to check database connection
        boolean isConnected = true; // replace with actual connection logic

        if (isConnected) {
            return Mono.just("Database connection successful!");
        } else {
            return Mono.empty(); // or Mono.error(new RuntimeException("Connection failed"));
        }
    }
}
