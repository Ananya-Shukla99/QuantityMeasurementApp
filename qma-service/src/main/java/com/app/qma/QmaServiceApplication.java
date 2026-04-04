package com.app.qma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // Register with Eureka service discovery
public class QmaServiceApplication {

    // Main entry point - starts the Spring Boot application
    public static void main(String[] args) {
        SpringApplication.run(QmaServiceApplication.class, args);
    }
}
