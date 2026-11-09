package me.satyajit.gatekeeper.management.service;


import me.satyajit.gatekeeper.management.dto.login.LoginRequestDto;
import me.satyajit.gatekeeper.management.dto.login.LoginResponseDto;
import me.satyajit.gatekeeper.management.entity.ManagementUsers;
import me.satyajit.gatekeeper.management.entity.OrganizationStatus;
import me.satyajit.gatekeeper.management.exception.ApiException;
import me.satyajit.gatekeeper.management.exception.InvalidCredentialsException;
import me.satyajit.gatekeeper.management.repository.ManagementUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthenticationService {

    private final ManagementUserRepository managementUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public AuthenticationService(ManagementUserRepository managementUserRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtEncoder jwtEncoder,
                                 @Value("${security.jwt.expiration-seconds}")
                                 long expirationsSeconds)
    {
        this.managementUserRepository = managementUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationsSeconds;
    }

    @Transactional(readOnly = true)
    public LoginResponseDto loginUser(LoginRequestDto requestUser){



        ManagementUsers existingUser =
                managementUserRepository.findByUsername(requestUser.getUsername())
                        .orElseThrow(() ->
                                new InvalidCredentialsException("Username or password Invalid"));

        if (!existingUser.isEnabled() ||
                passwordEncoder.matches(requestUser.getPassword(),
                        existingUser.getPasswordHash())){

            throw new InvalidCredentialsException("Username or password Invalid");
        }

        if (existingUser.getOrganization() != null &&
                existingUser.getOrganization().getStatus() != OrganizationStatus.ACTIVE){
            throw new ApiException(HttpStatus.FORBIDDEN, "Organization Is Not Active");
        }

        Instant issuedAt = Instant.now();
        String role = existingUser.getRole().getName();
        Long organizationId = existingUser.getOrganization() != null ?
                existingUser.getOrganization().getId() : null;

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer("gatekeeper-api-management")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(expirationSeconds))
                .subject(existingUser.getUsername())
                .claim("role", role);

        if (organizationId != null){
            claims.claim("organizationId", organizationId);
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header,
                        claims.build()))
                        .getTokenValue();

        LoginResponseDto response = new LoginResponseDto(
                token,
                "bearer",
                expirationSeconds,
                role,
                organizationId
        );

        return response;
    }

}
