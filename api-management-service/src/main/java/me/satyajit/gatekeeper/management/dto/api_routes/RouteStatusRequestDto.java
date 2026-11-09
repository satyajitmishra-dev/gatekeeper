package me.satyajit.gatekeeper.management.dto.api_routes;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteStatusRequestDto {

    @NotNull
    private Boolean setActive;
}
