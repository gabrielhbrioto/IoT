package com.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtToAuthenticationConverter jwtToAuthenticationConverter;  // Adicionando a injeção de JwtToAuthenticationConverter

    // Construtor para injeção de dependências
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtToAuthenticationConverter jwtToAuthenticationConverter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtToAuthenticationConverter = jwtToAuthenticationConverter;  // Inicializando
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/auth/login", "/auth/register").permitAll()
                .anyExchange().authenticated()
            )
            // Adicionando o filtro de autenticação JWT antes de outros filtros
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtToAuthenticationConverter))  // Corrigido: Usando a instância injetada
            );

        return http.build();
    }
}
