package me.satyajit.gatekeeper.management.dto.api_routes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class HeaderRuleRequest {


    @NotBlank(message = "Header direction is required")
    @Size(max = 20, message = "Header direction must not exceed 20 characters")
    private String direction;

    @NotBlank(message = "Header action is required")
    @Size(max = 20, message = "Header action must not exceed 20 characters")
    private String action;

    @NotBlank(message = "Header name is required")
    @Size(max = 200, message = "Header name must not exceed 200 characters")
    private String headerName;

    @Size(max = 2000, message = "Header value must not exceed 2000 characters")
    private String headerValue;

}
