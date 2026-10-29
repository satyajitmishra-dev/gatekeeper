package me.satyajit.gatekeeper.management.dto.healthResponseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class HealthResponseDto {

    private String appStatus;
    private String dbStatus;
    private LocalDateTime timestamp;

}
