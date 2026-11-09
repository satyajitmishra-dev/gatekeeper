package me.satyajit.gatekeeper.management.dto.organization;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;

@Getter
@Setter
public class OrganizationStatusRequestDto {

    @NotNull(message = "Organization cannot be null")
    private OrganizationStatus status;
}
