package me.satyajit.gatekeeper.management.controller.gateway;

import me.satyajit.gatekeeper.management.dto.api_routes.RoutesResponseDto;
import me.satyajit.gatekeeper.management.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gateway/routes")
public class GatewayConfigurationController {

    private final RouteService routeService;

    public GatewayConfigurationController(RouteService routeService){
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RoutesResponseDto>> getActiveRoutes(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(routeService.findAllActiveForGateway());
    }

}
