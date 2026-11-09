package me.satyajit.gatekeeper.management.service;

import me.satyajit.gatekeeper.management.dto.organization.OrganizationRequestDto;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationResponseDto;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationStatusRequestDto;
import me.satyajit.gatekeeper.management.dto.organization.TanentAdminOrganizationRequestDto;
import me.satyajit.gatekeeper.management.dto.tanent_admin.TanenetAdminRequestDto;
import me.satyajit.gatekeeper.management.dto.tanent_admin.TanenetAdminResponseDto;
import me.satyajit.gatekeeper.management.entity.ManagementUsers;
import me.satyajit.gatekeeper.management.entity.Organization;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;
import me.satyajit.gatekeeper.management.entity.Role;
import me.satyajit.gatekeeper.management.exception.ApiException;
import me.satyajit.gatekeeper.management.repository.ManagementUserRepository;
import me.satyajit.gatekeeper.management.repository.OrganizationRepository;
import me.satyajit.gatekeeper.management.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService {


    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ManagementUserRepository managementUserRepository;
    private final TenantAdminContext tenantAdminContext;
    private final AuditService auditService;
    public OrganizationService(OrganizationRepository organizationRepository,
                               PasswordEncoder passwordEncoder,
                               RoleRepository roleRepository,
                               ManagementUserRepository managementUserRepository,
                               TenantAdminContext tenantAdminContext,
                               AuditService auditService) {
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.managementUserRepository = managementUserRepository;
        this.tenantAdminContext = tenantAdminContext;
        this.auditService = auditService;
    }



    @Transactional
    public OrganizationResponseDto createOrganization(
            OrganizationRequestDto requestOrganization){

        long startedAt = System.nanoTime();

        checkUnique(requestOrganization.getName(), requestOrganization.getSlug(), null);

        Organization organization = new Organization();
        organization.setName(requestOrganization.getName());
        organization.setSlug(requestOrganization.getSlug());
        organization.setStatus(OrganizationStatus.PENDING);
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());


        organizationRepository.save(organization);
        auditService.record(organization,
                "CREATE_ORGANIZATION",
                "ORGANIZATION",
                organization.getId().toString(),
                startedAt,
                "Organization created at PENDING status");


        return OrganizationResponseDto.from(organization);

    }

    @Transactional(readOnly = true)
    public List<OrganizationResponseDto> getAllOrganization(){

        List<Organization> existingOrganizations =
                organizationRepository.findAllByStatusNot(OrganizationStatus.DELETED);

        List<OrganizationResponseDto> response = new ArrayList<>();

        existingOrganizations.forEach(organization ->
                response.add(
                        OrganizationResponseDto.from(organization)
                ));



        return response;
    }

    @Transactional
    public OrganizationResponseDto getOrganizationById(Long id){

        Organization existingOrganization = findOrganizationById(id);

        return OrganizationResponseDto.from(existingOrganization);

    }

@Transactional
    public OrganizationResponseDto updateOrganizationById(
            Long id, OrganizationRequestDto requestOrganization){

        Organization existingOrganization = findOrganizationById(id);

        checkUnique(requestOrganization.getName(), requestOrganization.getSlug(), id);
        existingOrganization.setName(requestOrganization.getName());
        existingOrganization.setSlug(requestOrganization.getSlug());
        existingOrganization.setUpdatedAt(LocalDateTime.now());

//        organizationRepository.save(existingOrganization);

        return OrganizationResponseDto.from(existingOrganization);
    }


    @Transactional
    public OrganizationResponseDto updateOrganizationStatusById(
            Long id,
            OrganizationStatusRequestDto requestStatus
    ){

        Organization existingOrganization = findOrganizationById(id);

        if (requestStatus.getStatus() == OrganizationStatus.DELETED){
            throw new ApiException(HttpStatus.FORBIDDEN, "Use delete features to delete");
        }

        existingOrganization.setStatus(requestStatus.getStatus());
        existingOrganization.setUpdatedAt(LocalDateTime.now());

        return OrganizationResponseDto.from(existingOrganization);
    }

    @Transactional
    public String deleteOrganizationById(Long id){

        Organization organizationToBeDeleted = findOrganizationById(id);

        organizationToBeDeleted.setStatus(OrganizationStatus.DELETED);
        organizationToBeDeleted.setDeletedAt(LocalDateTime.now());

        return "Organization deleted successfully";

    }


//====================================== TANENT ADMIN SERVICES ============================

    @Transactional
    public TanenetAdminResponseDto createTanentAdmin(Long organizationId,
                                                     TanenetAdminRequestDto requestAdmin){

        Organization existingOrganization = findOrganizationById(organizationId);

        if (managementUserRepository.findByOrganizationId(organizationId).isPresent()){
            throw new ApiException(HttpStatus.CONFLICT,
                    "In this organization already administrator present");
        }

        if (managementUserRepository.findByUsername(requestAdmin.getUsername()).isPresent()){
            throw new ApiException(HttpStatus.CONFLICT,
                    "Username already exists");
        }

        ManagementUsers managementUserToBeCreated = new ManagementUsers();

        Role role = roleRepository.findByName("TENANT_ADMIN").orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Role not exist")
        );

        managementUserToBeCreated.setUsername(requestAdmin.getUsername());
        managementUserToBeCreated.setPasswordHash(passwordEncoder
                .encode(
                        requestAdmin.getPassword()
                )
        );
        managementUserToBeCreated.setEnabled(true);
        managementUserToBeCreated.setOrganization(existingOrganization);
        managementUserToBeCreated.setRole(role);
        managementUserToBeCreated.setCreatedAt(LocalDateTime.now());
        managementUserToBeCreated.setUpdatedAt(LocalDateTime.now());

        managementUserRepository.save(managementUserToBeCreated);

        return TanenetAdminResponseDto
                .from(managementUserToBeCreated);
    }


    @Transactional(readOnly = true)
    public TanenetAdminResponseDto getTanentAdmin (Long organizationId){

        Organization existingOrg = findOrganizationById(organizationId);

        ManagementUsers user = getTanentAdminEntity(organizationId);
      return TanenetAdminResponseDto.from(user) ;
    }

    @Transactional
    public TanenetAdminResponseDto updateTanentAdmin (Long organizationId,
                                                      TanenetAdminRequestDto requestAdmin){

        ManagementUsers existingUser = getTanentAdminEntity(organizationId);

        if (!existingUser.getUsername().equals(requestAdmin.getUsername())
        && managementUserRepository.existsByUsername(requestAdmin.getUsername())){

            throw new ApiException(HttpStatus.CONFLICT,
                    "Username Already exists");
        }

        existingUser.setUsername(requestAdmin.getUsername());

        existingUser.setPasswordHash(passwordEncoder.encode(requestAdmin.getPassword()));
        existingUser.setUpdatedAt(LocalDateTime.now());

        return TanenetAdminResponseDto.from(existingUser);
    }

    @Transactional
    public String deleteTanentAdmin (Long organizationId){

        ManagementUsers userToBeDeleted = getTanentAdminEntity(organizationId);

       managementUserRepository.delete(userToBeDeleted);

        return "Deleted Successfully";
    }


//    ================================== HELPER METHODS =====================================
    public Organization findOrganizationById(Long id){
        return organizationRepository.findByIdAndStatusNot(id, OrganizationStatus.DELETED)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "Organization Not Exist"));
    }

    public void checkUnique (String name, String slug, Long currentId){

        if (organizationRepository.existsByNameIgnoreCase(name)){

            Organization current = currentId == null ? null
                    : organizationRepository.findById(currentId).orElse(null);

            if (current == null || !current.getName().equalsIgnoreCase(name)){
                throw  new ApiException(HttpStatus.CONFLICT,
                        "Organization name already exists");
            }
        }


        if (organizationRepository.existsBySlugIgnoreCase(slug)){

            Organization current = currentId == null ? null
                    : organizationRepository.findById(currentId).orElse(null);


            if (current == null || !current.getSlug().equalsIgnoreCase(slug)){

                throw new ApiException(HttpStatus.CONFLICT,
                        "Organization slug already exists");
            }
        }
    }

    public  ManagementUsers getTanentAdminEntity(Long organizationId){

        return managementUserRepository
                .findByOrganizationId(organizationId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Tanent admin not found"
                ));

    }

    // ========================== TANENT ADMIN ROUTES==============================

    @Transactional(readOnly = true)
    public OrganizationResponseDto getOwnOrganization(){

        return OrganizationResponseDto.from(
                tenantAdminContext.getOrganizaton()
        );
    }

    @Transactional
    public OrganizationResponseDto updateOwnOrganization(
            TanentAdminOrganizationRequestDto request
    ){
        Organization organizationToBeUpdated = tenantAdminContext.getOrganizaton();

        if (!request.getName().equals(organizationToBeUpdated.getName()) &&
                (organizationRepository.existsByNameIgnoreCase(request.getName()))){
            throw new ApiException(HttpStatus.CONFLICT,
                        "Organization name already exists");
            }
        organizationToBeUpdated.setName(request.getName());
        organizationToBeUpdated.setUpdatedAt(LocalDateTime.now());
        return OrganizationResponseDto.from(organizationToBeUpdated);
    }
}
