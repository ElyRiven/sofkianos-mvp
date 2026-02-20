package com.sofkianos.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Producer API application entry point.
 *
 * <p>Architecture principles:
 * <ul>
 *     <li>API Gateway Pattern: exposes the single inbound endpoint for Kudos.</li>
 *     <li>Asynchronous Messaging: hands off payloads to RabbitMQ for downstream processing.</li>
 *     <li>Separation of Concerns: bootstraps the app while messaging and web layers stay isolated.</li>
 * </ul>
 */
@SpringBootApplication
@EnableCaching
public class ProducerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApiApplication.class, args);
    }
}