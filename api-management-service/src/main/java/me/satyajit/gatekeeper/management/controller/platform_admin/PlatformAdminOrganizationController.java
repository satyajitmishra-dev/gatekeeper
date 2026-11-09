package me.satyajit.gatekeeper.management.controller.platform_admin;

import jakarta.validation.Valid;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationRequestDto;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationResponseDto;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationStatusRequestDto;
import me.satyajit.gatekeeper.management.dto.tanent_admin.TanenetAdminRequestDto;
import me.satyajit.gatekeeper.management.dto.tanent_admin.TanenetAdminResponseDto;
import me.satyajit.gatekeeper.management.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform/organizations")
public class PlatformAdminOrganizationController {

    private final OrganizationService organizationService;

    public PlatformAdminOrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // CRUD -> Organization && Tenant Admin

    // ORGANIZATION OPERATIONS

    // 1. Create
    @PostMapping
    public ResponseEntity<OrganizationResponseDto> createOrganization(
            @Valid @RequestBody OrganizationRequestDto requestOrganization
            ){

        OrganizationResponseDto response =
                organizationService.createOrganization(requestOrganization);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 2. Read all

    @GetMapping
    public ResponseEntity<List<OrganizationResponseDto>> getAllOrganization(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.getAllOrganization());
    }

    // 3. Read one

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> getOrganizationById(@PathVariable Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.getOrganizationById(id));
    }
    // 4. Update

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> updateOrganizationById(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequestDto requestOrganization){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.updateOrganizationById(id, requestOrganization));
    }


    // 5. Patch
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrganizationResponseDto>
    updateOrganizationStatusById(@PathVariable Long id,
                                 @RequestBody OrganizationStatusRequestDto requestDto){

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(organizationService.updateOrganizationStatusById(id, requestDto));
    }

        // 6. Delete (Soft-delete)
        @PatchMapping("/{id}")
        public ResponseEntity<String> deleteOrganizationById(
                @PathVariable Long id
        ){

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(organizationService.deleteOrganizationById(id));
        }


//        ============================== TENANT ADMIN =================================

    @PostMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<TanenetAdminResponseDto> createTanentAdmin(
            @PathVariable Long organizationId,
            @Valid @RequestBody TanenetAdminRequestDto requestTanent
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(organizationService.createTanentAdmin(organizationId, requestTanent));
    }

    @GetMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<TanenetAdminResponseDto> getTanentAdmin(
            @PathVariable Long organizationId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationService.getTanentAdmin(organizationId));
    }

    @PutMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<TanenetAdminResponseDto> updateTanantAdmin(@PathVariable Long organizationId,
                                                                 @Valid @RequestBody TanenetAdminRequestDto request) {
        return ResponseEntity.ok(organizationService.updateTanentAdmin(organizationId, request));
    }

    @DeleteMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<Void> deleteTanantAdmin(@PathVariable Long organizationId) {
        organizationService.deleteTanentAdmin(organizationId);
        return ResponseEntity.noContent().build();
    }
}
