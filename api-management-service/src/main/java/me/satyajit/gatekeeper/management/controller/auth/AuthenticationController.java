package me.satyajit.gatekeeper.management.controller.auth;


import jakarta.validation.Valid;
import me.satyajit.gatekeeper.management.dto.login.LoginRequestDto;
import me.satyajit.gatekeeper.management.dto.login.LoginResponseDto;
import me.satyajit.gatekeeper.management.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest
    ){

        LoginResponseDto response =
                authenticationService.loginUser(loginRequest);

        return ResponseEntity
                .ok(response);
    }

}
