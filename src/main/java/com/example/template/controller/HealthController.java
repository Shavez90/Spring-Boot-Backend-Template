package com.example.template.controller;

import com.example.template.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<?>> health() {
        log.info("Health check endpoint called");
        Map<String, String> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("service", "Spring Boot Backend Template");
        
        ApiResponse<?> response = new ApiResponse<>(true, "Application is running successfully", healthData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/live")
    public ResponseEntity<ApiResponse<?>> live() {
        log.info("Liveness check endpoint called");
        Map<String, String> liveData = new HashMap<>();
        liveData.put("status", "ALIVE");
        
        ApiResponse<?> response = new ApiResponse<>(true, "Application is live", liveData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ready")
    public ResponseEntity<ApiResponse<?>> ready() {
        log.info("Readiness check endpoint called");
        Map<String, String> readyData = new HashMap<>();
        readyData.put("status", "READY");
        readyData.put("database", "Connected");
        
        ApiResponse<?> response = new ApiResponse<>(true, "Application is ready to accept requests", readyData);
        return ResponseEntity.ok(response);
    }
}
