package me.satyajit.gatekeeper.management.dto.organization;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequestDto {

    @NotBlank
    @Size(min = 4,max = 100, message = "Organization name must be 4-100 character long")
    private String name;

    @NotBlank(message = "Organization slug is required")
    @Pattern(
            regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
            message = "Organization slug must contain only lowercase letters, numbers, and single hyphens"
    )
    private String slug;


}
