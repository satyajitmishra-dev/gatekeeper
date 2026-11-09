package me.satyajit.gatekeeper.management.dto.api_routes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.satyajit.gatekeeper.management.entity.ApiRoutes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RoutesResponseDto {

    private Long id;
    private Long organizationId;
    private String routeId;
    private String name;
    private String pathPattern;
    private String targetBaseUrl;
    private String targetPathPrefix;
    private Integer requestsPerMinute;
    private Integer responseTimeoutMs;
    private boolean idempotencyEnabled;
    private boolean active;
    private Long configurationVersion;
    private Set<String> allowedMethods;
    private List<HeaderRulesResponse> headerRules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static RoutesResponseDto from(ApiRoutes routes) {

        return new RoutesResponseDto(
                routes.getId(),
                routes.getOrganization().getId(),
                routes.getRouteId(),
                routes.getName(),
                routes.getPathPattern(),
                routes.getTargetBaseUrl(),
                routes.getTargetPathPrefix(),
                routes.getRequestsPerMinute(),
                routes.getResponseTimeoutMs(),
                routes.isIdempotencyEnabled(),
                routes.isActive(),
                routes.getConfigurationVersion(),
                routes.getAllowedMethods(),
               routes.getHeaderRules().stream()
                       .map(HeaderRulesResponse::from)
                       .toList(),
                routes.getCreatedAt(),
                routes.getUpdatedAt()
        );
    }

}
