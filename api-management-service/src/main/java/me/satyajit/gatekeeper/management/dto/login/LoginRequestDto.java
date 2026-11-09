package me.satyajit.gatekeeper.management.dto.login;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "username required")
    @Size(min = 3, message = "username must be 3 character")
    private String username;

    @NotBlank(message = "password required")
    @Size(min = 8, message = "password must be 8 character")
    private String password;
}
