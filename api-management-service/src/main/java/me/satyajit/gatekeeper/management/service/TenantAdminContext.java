package me.satyajit.gatekeeper.management.service;

import me.satyajit.gatekeeper.management.entity.Organization;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;
import me.satyajit.gatekeeper.management.exception.ApiException;
import me.satyajit.gatekeeper.management.exception.ResourceNotFoundException;
import me.satyajit.gatekeeper.management.repository.OrganizationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class TenantAdminContext {

    private OrganizationRepository organizationRepository;

    public TenantAdminContext(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization getOrganizaton(){

        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken)){
            throw new ApiException(HttpStatus.FORBIDDEN, "Tenant admin identity invalid");
        }

        Long organizationId = ((JwtAuthenticationToken) authentication).getToken()
                .getClaim("organizationId");

        if (organizationId == null){
            throw new ApiException(HttpStatus.FORBIDDEN, "Tenant admin identity invalid");
        }

        Organization existingOrganization =
                organizationRepository.findById(organizationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Organization not found"));

        if (existingOrganization.getStatus() != OrganizationStatus.ACTIVE){
            throw new ApiException(HttpStatus.FORBIDDEN, "Organization not active");
        }

        return existingOrganization;

    }
}
