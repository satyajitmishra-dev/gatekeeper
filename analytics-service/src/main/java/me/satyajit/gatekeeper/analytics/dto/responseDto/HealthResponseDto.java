package me.satyajit.gatekeeper.analytics.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HealthResponseDto {

    private String status;
    private String serviceName;
    private String serviceDescription;

}
