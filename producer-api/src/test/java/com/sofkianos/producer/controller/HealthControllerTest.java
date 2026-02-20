package com.sofkianos.producer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    void health_ReturnsOkWithStatusMessage() {
        // Act
        ResponseEntity<Map<String, String>> response = healthController.health();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
    }
}
