package me.satyajit.gatekeeper.management.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.satyajit.gatekeeper.management.entity.Organization;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrganizationResponseDto {

    private Long id;
    private String name;
    private String slug;
    private OrganizationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrganizationResponseDto from (Organization organization){

        return new OrganizationResponseDto(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getStatus(),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }

}
