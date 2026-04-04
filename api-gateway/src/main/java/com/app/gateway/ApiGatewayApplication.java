package com.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway Application - Single Entry Point
 * This gateway acts as a single entry point for all client requests.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    // Main entry point - starts the Spring Boot application
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

