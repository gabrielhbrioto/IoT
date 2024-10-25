    package com.iot.config;

    import org.springframework.security.authentication.ReactiveAuthenticationManager;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder; // Alterado para ReactiveJwtDecoder
    import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
    import org.springframework.stereotype.Component;
    import org.springframework.web.server.ServerWebExchange;
    import reactor.core.publisher.Mono;

    @Component
    public class JwtAuthenticationFilter extends AuthenticationWebFilter {

        private final ReactiveJwtDecoder jwtDecoder; // Alterado para ReactiveJwtDecoder

        public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, JwtToAuthenticationConverter converter, ReactiveJwtDecoder jwtDecoder) {
            super(authenticationManager);
            this.jwtDecoder = jwtDecoder;

            // Configura o conversor JWT diretamente
            setServerAuthenticationConverter(exchange -> {
                String token = extractToken(exchange); // Extração de token
                if (token != null) {
                    return jwtDecoder.decode(token) // Decodifica o token JWT de forma reativa
                            .flatMap(jwt -> converter.convert(jwt).map(auth -> (Authentication) auth)) // Converte para Authentication
                            .onErrorResume(e -> Mono.empty()); // Retorna vazio em caso de erro
                }
                return Mono.empty(); // Retorna vazio se o token não estiver presente
            });
        }

        private String extractToken(ServerWebExchange exchange) {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                return token.substring(7); // Remove o prefixo "Bearer " do token
            }
            return null;
        }
    }
