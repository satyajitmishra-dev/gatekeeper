package me.satyajit.gatekeeper.management.controller.healthController;


import me.satyajit.gatekeeper.management.dto.healthResponseDto.HealthResponseDto;
import me.satyajit.gatekeeper.management.service.HealthCheckupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HealthController {

    private HealthCheckupService healthCheckupService;

    public HealthController(HealthCheckupService healthCheckupService){
        this.healthCheckupService = healthCheckupService;
    }

    @GetMapping("/health")

    public ResponseEntity<HealthResponseDto> healthCheck(){


        HealthResponseDto response = new HealthResponseDto(
                "UP",
                healthCheckupService.getHealth(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .ok(response);
    }
}
