package com.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

/**
 * Configuração de segurança para a aplicação.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtToAuthenticationConverter jwtToAuthenticationConverter;

    /**
     * Construtor para injetar dependências.
     *
     * @param jwtAuthenticationFilter Filtro de autenticação JWT.
     * @param jwtToAuthenticationConverter Conversor de JWT para autenticação.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtToAuthenticationConverter jwtToAuthenticationConverter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtToAuthenticationConverter = jwtToAuthenticationConverter;
    }

    /**
     * Configura a cadeia de filtros de segurança da web.
     *
     * @param http Configuração de segurança HTTP do servidor.
     * @return A cadeia de filtros de segurança configurada.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Desabilita a proteção CSRF.
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(List.of("*")); // Permite todas as origens.
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Permite todos os métodos HTTP.
                configuration.setAllowedHeaders(List.of("*")); // Permite todos os cabeçalhos.
                configuration.setAllowCredentials(true); // Permite credenciais.
                return configuration;
            }))
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/auth/login", "/usuarios").permitAll() // Permite acesso público a estas rotas.
                .anyExchange().authenticated() // Requer autenticação para qualquer outra rota.
            )
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Adiciona o filtro de autenticação JWT.
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtToAuthenticationConverter)) // Configura o conversor de autenticação JWT.
            );

        return http.build();
    }
}
