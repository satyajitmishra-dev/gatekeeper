package me.satyajit.gatekeeper.management.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder(
            @Value("${security.jwt.secret}") String secret
    ){

        SecretKey secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusJwtEncoder.withSecretKey(secretKey)
                .algorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret){

        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    public Converter<Jwt, AbstractAuthenticationToken> jwuAuthenticationConverter(){

        return jwt -> {
            String role = jwt.getClaimAsString("role");
            return new JwtAuthenticationToken(jwt,
                    Collections.singletonList(new SimpleGrantedAuthority(
                            "ROLE_" +role
                    )),
                    jwt.getSubject());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity){

        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/health",
                                "/api/auth/login",
                                        "/", "/index.html", "/style.css", "/app.js")
                                .permitAll()
                                .requestMatchers("/api/platform/**").hasRole("PLATFORM_ADMIN")
                                .requestMatchers("/api/tanent/**").hasRole("TENANT_ADMIN")
                                .requestMatchers("/api/gateway/**").hasRole("GATEWAY_SERVICE")
                                .anyRequest()
                                .authenticated())
                .oauth2ResourceServer(resourceServer ->
                       resourceServer.jwt(jwtConfigurer ->
                               jwtConfigurer
                                       .jwtAuthenticationConverter(
                                               jwuAuthenticationConverter())));

        return httpSecurity.build();
    }
}
