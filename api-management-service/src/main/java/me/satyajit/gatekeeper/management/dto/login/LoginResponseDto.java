package me.satyajit.gatekeeper.management.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {


    private String token;
    private String tokenType;
    private long expiresInSecond;
    private String role;
    private Long organizationId;
}
