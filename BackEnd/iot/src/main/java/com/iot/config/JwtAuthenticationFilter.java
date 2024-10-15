package com.iot.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.stereotype.Component;

@Component // Adiciona esta anotação
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, JwtAuthenticationConverter converter) {
        super(authenticationManager);

        setServerAuthenticationConverter(exchange -> {
            String token = extractToken(exchange);
            return Mono.justOrEmpty(token)
                .flatMap(converter::convert);
        });
    }

    private String extractToken(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Remove "Bearer " do token
        }
        return null;
    }
}
