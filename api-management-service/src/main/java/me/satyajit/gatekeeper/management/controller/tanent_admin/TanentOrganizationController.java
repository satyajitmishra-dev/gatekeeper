package me.satyajit.gatekeeper.management.controller.tanent_admin;

import jakarta.validation.Valid;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationResponseDto;
import me.satyajit.gatekeeper.management.dto.organization.TanentAdminOrganizationRequestDto;
import me.satyajit.gatekeeper.management.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tanent/organization")
public class TanentOrganizationController {

    private OrganizationService organizationService;

    public TanentOrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @GetMapping
    public ResponseEntity<OrganizationResponseDto> getOwnOrganization(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.getOwnOrganization());
    }

    @PutMapping
    public ResponseEntity<OrganizationResponseDto> updateOwnOrganization(
            @Valid @RequestBody TanentAdminOrganizationRequestDto request
            ){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.updateOwnOrganization(request));
    }
}
