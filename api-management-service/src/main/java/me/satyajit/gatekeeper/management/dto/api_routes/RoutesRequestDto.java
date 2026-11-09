package me.satyajit.gatekeeper.management.dto.api_routes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RoutesRequestDto {


    @NotBlank(message = "Route ID is required")
    @Size(max = 100, message = "Route ID must not exceed 100 characters")
    private String routeId;

    @NotBlank(message = "Route name is required")
    @Size(max = 200, message = "Route name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Path pattern is required")
    @Size(max = 500, message = "Path pattern must not exceed 500 characters")
    private String pathPattern;

    @NotBlank(message = "Target base URL is required")
    @Size(max = 1000, message = "Target base URL must not exceed 1000 characters")
    private String targetBaseUrl;

    @NotBlank(message = "Target path prefix is required")
    @Size(max = 500, message = "Target path prefix must not exceed 500 characters")
    private String targetPathPrefix;

    @NotNull(message = "Requests per minute is required")
    @Min(value = 1, message = "Requests per minute must be at least 1")
    private Integer requestsPerMinute;

    @NotNull(message = "Response timeout is required")
    @Min(value = 1, message = "Response timeout must be at least 1 millisecond")
    private Integer responseTimeoutMs;

    private boolean idempotencyEnabled = false;

    private boolean active = true;

    @NotEmpty(message = "At least one allowed HTTP method must be selected")
    private Set<
            @NotBlank(message = "HTTP method cannot be blank")
                    String
            > allowedMethods = new HashSet<>();

    @NotNull(message = "Header rules list cannot be null")
    private List<@Valid HeaderRuleRequest> headerRules = new ArrayList<>();

}
