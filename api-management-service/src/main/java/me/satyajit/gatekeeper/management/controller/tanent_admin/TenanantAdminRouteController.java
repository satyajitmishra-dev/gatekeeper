package me.satyajit.gatekeeper.management.controller.tanent_admin;


import jakarta.validation.Valid;
import me.satyajit.gatekeeper.management.dto.api_routes.RouteStatusRequestDto;
import me.satyajit.gatekeeper.management.dto.api_routes.RouteVersionResponse;
import me.satyajit.gatekeeper.management.dto.api_routes.RoutesRequestDto;
import me.satyajit.gatekeeper.management.dto.api_routes.RoutesResponseDto;
import me.satyajit.gatekeeper.management.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tanent")
public class TenanantAdminRouteController {

    private final RouteService routeService;

    public TenanantAdminRouteController (RouteService routeService){
        this.routeService = routeService;
    }


//   ================================== CRUD ROUTES =========================

    // ================================ 1.  CREATE --> ROUTE =============================
    @PostMapping("/routes")
    public ResponseEntity<RoutesResponseDto> createRoutes(@Valid @RequestBody
                                                          RoutesRequestDto request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRouts(request));
    }

    // ================================ 2.READ ONE/ALL ================================

    @GetMapping("/routes/{id}")
    public ResponseEntity<RoutesResponseDto> getRoute (@PathVariable Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(routeService.getRouteById(id));
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RoutesResponseDto>> getAllRoutes(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(routeService.getAllRoutes());
    }

    // ================================ 3.UPDATE ================================

    @PutMapping("/routes/{id}")
    public ResponseEntity<RoutesResponseDto> updateRoute (@PathVariable Long id,
                                                          @Valid @RequestBody RoutesRequestDto
                                                                  request){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(routeService.updateRouteById(id, request));
    }
    // ================================ 4. DELETE ================================

   @DeleteMapping("/routes/{id}")

    public ResponseEntity<String> deleteRoute(@PathVariable Long id) {

    return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(routeService.deleteRouteById(id));
    }

    // ================================ 5. UPDATE STATUS =================
    @PatchMapping("/routes/{id}/status")
    public ResponseEntity<RoutesResponseDto> updateRouteStatus (@RequestBody
                                                                RouteStatusRequestDto request,
                                                               @PathVariable Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(routeService.changeRouteStatus(id, request));

    }

    @GetMapping("/route-versions/{routeId}")
    public ResponseEntity<List<RouteVersionResponse>> getVersions (
            @PathVariable String routeId
    ){


        return ResponseEntity.status(HttpStatus.OK)
                .body(routeService.getVersions(routeId));
    }


}
