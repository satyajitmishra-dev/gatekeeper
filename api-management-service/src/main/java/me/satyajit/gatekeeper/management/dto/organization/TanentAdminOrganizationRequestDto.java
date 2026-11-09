package me.satyajit.gatekeeper.management.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TanentAdminOrganizationRequestDto {

    @NotBlank(message = "Organization name required")
    @Size(min = 4,max = 100, message = "Organization name must be 4-100 character long")
    private String name;
}
