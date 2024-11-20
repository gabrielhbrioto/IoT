package com.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtToAuthenticationConverter jwtToAuthenticationConverter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtToAuthenticationConverter jwtToAuthenticationConverter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtToAuthenticationConverter = jwtToAuthenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();

                // Permite qualquer origem, mesmo com credenciais
                configuration.setAllowedOriginPatterns(List.of("*"));

                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                return configuration;
            }))
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/auth/login", "/usuarios").permitAll()
                .anyExchange().authenticated()
            )
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtToAuthenticationConverter))
            );

        return http.build();
    }
}
