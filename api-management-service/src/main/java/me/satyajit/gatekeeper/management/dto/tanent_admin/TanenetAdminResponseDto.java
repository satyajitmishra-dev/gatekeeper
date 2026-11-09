package me.satyajit.gatekeeper.management.dto.tanent_admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.satyajit.gatekeeper.management.dto.organization.OrganizationResponseDto;
import me.satyajit.gatekeeper.management.entity.ManagementUsers;

@Getter
@Setter
@AllArgsConstructor
public class TanenetAdminResponseDto {

    private Long id;
    private Long organizationId;
    private String username;
    private boolean enabled;


    public static TanenetAdminResponseDto from (ManagementUsers users){
        return new TanenetAdminResponseDto(
                users.getId(),
                users.getOrganization().getId(),
                users.getUsername(),
                users.isEnabled()
        );
    }
}
