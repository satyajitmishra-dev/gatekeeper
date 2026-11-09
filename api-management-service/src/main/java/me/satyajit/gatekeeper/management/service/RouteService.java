package me.satyajit.gatekeeper.management.service;

import me.satyajit.gatekeeper.management.dto.api_routes.*;
import me.satyajit.gatekeeper.management.entity.*;
import me.satyajit.gatekeeper.management.exception.ApiException;
import me.satyajit.gatekeeper.management.repository.ApiRoutesRepository;
import me.satyajit.gatekeeper.management.repository.RouteConfigurationVersionRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RouteService {


    private final ApiRoutesRepository apiRoutesRepository;
    private final TenantAdminContext tenantAdminContext;
    private final RouteConfigurationVersionRepository versionRepository;
    private final ObjectMapper objectMapper;

    public RouteService(ApiRoutesRepository apiRoutesRepository,
                        TenantAdminContext tenantAdminContext,
                        RouteConfigurationVersionRepository versionRepository,
                        ObjectMapper objectMapper) {
        this.apiRoutesRepository = apiRoutesRepository;
        this.tenantAdminContext = tenantAdminContext;
        this.versionRepository = versionRepository;
        this.objectMapper=objectMapper;
    }

    @Transactional
    public RoutesResponseDto createRouts(RoutesRequestDto requestRoutes){

        Organization organization = tenantAdminContext.getOrganizaton();
        validate(organization, requestRoutes);

        if (apiRoutesRepository.existsByOrganizationIdAndRouteId(organization.getId(),
                requestRoutes.getRouteId())){
            throw new ApiException(HttpStatus.CONFLICT,
                    "RouteID already exists for this organization");
        }

        ApiRoutes routes = new ApiRoutes();
        routes.setOrganization(organization);
        routes.setConfigurationVersion(1L);
        routes.setCreatedAt(LocalDateTime.now());
        apply(routes, requestRoutes);

        apiRoutesRepository.save(routes);

        saveRouteConfigurationVersion(routes, "CREATED");

        return RoutesResponseDto.from(routes);
    }

    @Transactional(readOnly = true)
    public List<RoutesResponseDto> getAllRoutes(){
        Organization organization = tenantAdminContext.getOrganizaton();

        return toResponses(
                apiRoutesRepository.findAllByOrganizationId(organization.getId())
        );
    }


    @Transactional(readOnly = true)
    public RoutesResponseDto getRouteById(Long routeId){

        Organization organization = tenantAdminContext.getOrganizaton();
        return RoutesResponseDto.from(getOwnedRoutes(routeId, organization.getId()));
    }

    @Transactional
    public RoutesResponseDto updateRouteById(Long id, RoutesRequestDto requestRoute){

        Organization organization = tenantAdminContext.getOrganizaton();
        ApiRoutes route = getOwnedRoutes(id, organization.getId());

        if (!route.getId().equals(requestRoute.getRouteId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "RouteID cannot be changed");
        }

        validate(organization, requestRoute);

        route.setConfigurationVersion(route.getConfigurationVersion() + 1);
        apply(route, requestRoute);

        saveRouteConfigurationVersion(route, "UPDATED");

        return RoutesResponseDto.from(route);
    }

    @Transactional
    public String deleteRouteById(Long id){

        Organization organization = tenantAdminContext.getOrganizaton();
        ApiRoutes route = getOwnedRoutes(id, organization.getId());

        route.setConfigurationVersion(route.getConfigurationVersion() + 1);
        route.setUpdatedAt(LocalDateTime.now());
        apiRoutesRepository.delete(route);
        saveRouteConfigurationVersion(route, "DELETED");

        return "Route deleted successfully";
    }

    @Transactional
    public RoutesResponseDto changeRouteStatus(Long id, RouteStatusRequestDto requestStatus){

        Organization organization = tenantAdminContext.getOrganizaton();
        ApiRoutes route = getOwnedRoutes(id, organization.getId());
        route.setActive(requestStatus.getSetActive());
        route.setConfigurationVersion(route.getConfigurationVersion() + 1);
        route.setUpdatedAt(LocalDateTime.now());

        String changeType = route.isActive() ? "ACTIVATED" : "DEACTIVATED";

        saveRouteConfigurationVersion(route, changeType);


        return RoutesResponseDto.from(route);
    }

    @Transactional(readOnly = true)
    public List<RouteVersionResponse> getVersions(String routeId){

        Organization organization = tenantAdminContext.getOrganizaton();

        List<RouteVersionResponse> response = new ArrayList<>();


        List<RouteConfigurationVersion> existingVersions = versionRepository
                .findAllByOrganizationIdAndRouteIdOrderByVersionNumberDesc(organization.getId(),
                        routeId);

        for (RouteConfigurationVersion version: existingVersions){
            response.add(RouteVersionResponse.from(version));
        }

        return response;
    }

//    =========================== HELPER METHODS ===================================

    private void validate(Organization organization,
                         RoutesRequestDto routesRequest){

        String tenantPrefix = "/" + organization.getSlug().trim() + "/";
        String pathPattern = routesRequest.getPathPattern();

        if (pathPattern == null ||
                !pathPattern.trim().startsWith(tenantPrefix)) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Path pattern must start with the tenant prefix " + tenantPrefix
            );
        }

        try {
            URI uri = URI.create(routesRequest.getTargetBaseUrl());
            if (!"http".equalsIgnoreCase(uri.getScheme()) &&
                    "https".equalsIgnoreCase(uri.getScheme())){

              throw new IllegalArgumentException();
            }

    } catch (IllegalArgumentException e){
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Target base URL must be a valid HTTP or HTTPS URL");
        }

        normalizeMethods(routesRequest.getAllowedMethods());

        for (HeaderRuleRequest rule : routesRequest.getHeaderRules()){

            String direction = rule.getDirection().toUpperCase();
            String action = rule.getAction().toUpperCase();

            if (!"REQUEST".equals(direction) && !"RESPONSE".equals(direction)){
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Header direction either REQUEST or RESPONSE");
            }

            if (!"ADD".equals(action) && !"REMOVE".equals(action)){
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Header action either ADD or REMOVE");
            }

            if ("ADD".equals(action) &&
                    (rule.getHeaderValue() == null || rule.getHeaderValue().trim().isEmpty())){
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "ADD header rules must required header value");
            }
        }

        }

        private static void apply (ApiRoutes routes, RoutesRequestDto request){

        routes.setAllowedMethods(request.getAllowedMethods());
        routes.setIdempotencyEnabled(request.isIdempotencyEnabled());
        routes.setName(request.getName());
        routes.setTargetPathPrefix(request.getTargetPathPrefix());
        routes.setRequestsPerMinute(request.getRequestsPerMinute());
        routes.setTargetBaseUrl(request.getTargetBaseUrl());
        routes.setRouteId(request.getRouteId());
        routes.setResponseTimeoutMs(request.getResponseTimeoutMs());
        routes.setPathPattern(request.getPathPattern());
        routes.setActive(request.isActive());
        routes.setUpdatedAt(LocalDateTime.now());

        routes.getHeaderRules().clear();

        for (HeaderRuleRequest ruleRequest : request.getHeaderRules()){
            RouteHeaderRules rules = new RouteHeaderRules();
            rules.setRoute(routes);
            rules.setDirection(ruleRequest.getDirection());
            rules.setAction(ruleRequest.getAction());
            rules.setAction(ruleRequest.getAction());
            rules.setHeaderValue(ruleRequest.getHeaderValue());
            rules.setCreatedAt(LocalDateTime.now());
            rules.setUpdatedAt(LocalDateTime.now());

            routes.getHeaderRules().add(rules);
        }

        }

        private static Set<String> normalizeMethods (Set<String> methods){

        Set<String> normalized = new HashSet<>();


        for (String method : methods){
            String value = method.toUpperCase();

            try {
                HttpMethod.valueOf(value);
            } catch (IllegalArgumentException e){
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Unsupported HTTP methods" +method);
            }

            normalized.add(value);
        }

        return normalized;

        }

        private ApiRoutes getOwnedRoutes(Long id, Long organizationId){
        return apiRoutesRepository.findByIdAndOrganizationId(id, organizationId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.BAD_REQUEST, "Route not found"));
        }

        private static List<RoutesResponseDto>  toResponses(List<ApiRoutes> routes){

        List<RoutesResponseDto> response = new ArrayList<>();

        for (ApiRoutes apiRoutes: routes){
            response.add(RoutesResponseDto.from(apiRoutes));
        }
        return response;
        }

//        =================================  GATEWAY METHODS  =====================================

    @Transactional(readOnly = true)
    public List<RoutesResponseDto> findAllActiveForGateway(){

       List<ApiRoutes> allExistingRoutes =
               apiRoutesRepository.findAllByActiveAndOrganizationStatus(
                       true,
                       OrganizationStatus.ACTIVE
               );

       return toResponses(allExistingRoutes);
    }


    public void saveRouteConfigurationVersion(ApiRoutes routes, String changeType){

        RouteConfigurationVersion configurationVersion = new RouteConfigurationVersion();
        configurationVersion.setId(routes.getId());
        configurationVersion.setOrganization(routes.getOrganization());
        configurationVersion.setRouteId(routes.getRouteId());
        configurationVersion.setChangeType(changeType);
        configurationVersion.setConfigurationSnapshot(
                objectMapper.writeValueAsString(RoutesResponseDto.from(routes))
        );
        configurationVersion.setVersionNumber(routes.getConfigurationVersion());
        configurationVersion.setCreatedAt(LocalDateTime.now());

        versionRepository.save(configurationVersion);

    }



}
