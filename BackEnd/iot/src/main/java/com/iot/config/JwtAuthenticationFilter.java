    package com.iot.config;

    import org.springframework.security.authentication.ReactiveAuthenticationManager;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
    import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
    import org.springframework.stereotype.Component;
    import org.springframework.web.server.ServerWebExchange;
    import reactor.core.publisher.Mono;

    /**
     * Filtro de autenticação JWT que estende AuthenticationWebFilter.
     * 
     * Este filtro extrai o token JWT do cabeçalho Authorization da requisição,
     * decodifica-o e converte-o em uma autenticação.
     */
    @Component
    public class JwtAuthenticationFilter extends AuthenticationWebFilter {

        private final ReactiveJwtDecoder jwtDecoder; 

        /**
         * Construtor para JwtAuthenticationFilter.
         *
         * @param authenticationManager o gerenciador de autenticação reativa
         * @param converter o conversor de JWT para autenticação
         * @param jwtDecoder o decodificador reativo de JWT
         */
        public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, JwtToAuthenticationConverter converter, ReactiveJwtDecoder jwtDecoder) {
            super(authenticationManager);
            this.jwtDecoder = jwtDecoder;

            // Define o conversor de autenticação do servidor
            setServerAuthenticationConverter(exchange -> {
                String token = extractToken(exchange); // Extrai o token JWT do cabeçalho Authorization
                if (token != null) {
                    return jwtDecoder.decode(token) // Decodifica o token JWT
                            .flatMap(jwt -> converter.convert(jwt).map(auth -> (Authentication) auth)) // Converte o JWT decodificado em autenticação
                            .onErrorResume(e -> Mono.empty()); // Retorna vazio em caso de erro
                }
                return Mono.empty(); // Retorna vazio se o token não estiver presente ou não começar com "Bearer "
            });
        }

        /**
         * Extrai o token JWT do cabeçalho Authorization da requisição.
         *
         * @param exchange o ServerWebExchange contendo a requisição
         * @return o token JWT extraído, ou null se o token não estiver presente ou não começar com "Bearer "
         */
        private String extractToken(ServerWebExchange exchange) {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                return token.substring(7); // Remove o prefixo "Bearer " do token
            }
            return null;
        }
    }
