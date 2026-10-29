package me.satyajit.gatekeeper.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity
    ){

        httpSecurity
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/health")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                );

        return httpSecurity.build();
    }
}
