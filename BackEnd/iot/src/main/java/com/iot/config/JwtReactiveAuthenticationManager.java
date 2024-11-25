package com.iot.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import com.iot.config.JwtUtil;

import java.util.Collections;
import java.util.List;

/**
 * JwtReactiveAuthenticationManager é um gerenciador de autenticação reativa que utiliza JWT.
 * Ele implementa a interface ReactiveAuthenticationManager.
 */
@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveJwtDecoder jwtDecoder; 
    private final JwtUtil jwtUtil;

    /**
     * Construtor que inicializa o decodificador JWT e a utilidade JWT.
     *
     * @param jwtDecoder o decodificador JWT reativo
     * @param jwtUtil a utilidade JWT para validação e extração de informações do token
     */
    public JwtReactiveAuthenticationManager(ReactiveJwtDecoder jwtDecoder, JwtUtil jwtUtil) {
        this.jwtDecoder = jwtDecoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Método de autenticação que valida o token JWT e retorna uma instância de Authentication.
     *
     * @param authentication a autenticação contendo as credenciais JWT
     * @return um Mono contendo a autenticação se o token for válido, ou um Mono vazio se não for
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getCredentials();
        String token = jwt.getTokenValue();  

        if (token == null) {
            return Mono.empty();
        }

        if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            Long userId = jwtUtil.extractUserId(token);
            String email = jwtUtil.extractUsername(token);

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, authorities);

            auth.setAuthenticated(true);
            return Mono.just(auth);
        }

        return Mono.empty();
    }

}
