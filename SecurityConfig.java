package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ✅ Corrected CSRF disabling
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/register").permitAll() // ✅ Publicly accessible
                .anyRequest().authenticated() // ✅ Other requests require authentication
            )
            .httpBasic(httpBasic -> {}); // ✅ Enables basic authentication

        return http.build();
    }

    @Bean
    public <PasswordEncoder> BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // ✅ Provides a BCrypt password encoder
    }
}
