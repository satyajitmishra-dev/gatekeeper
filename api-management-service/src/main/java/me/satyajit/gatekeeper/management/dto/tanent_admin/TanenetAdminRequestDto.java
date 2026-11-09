package me.satyajit.gatekeeper.management.dto.tanent_admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TanenetAdminRequestDto {

    @NotBlank(message = "username required")
    @Size(min = 3, max = 20, message = "username must be 3-20 character")
     private String username;

    @NotBlank(message = "password required")
    @Size(min = 8, message = "password minimum 8 character")
     private String password;
     private boolean status = true;

}
