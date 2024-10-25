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

//apagar:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveJwtDecoder jwtDecoder; // Mantido ReactiveJwtDecoder
    private final JwtUtil jwtUtil;
    //apagar
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public JwtReactiveAuthenticationManager(ReactiveJwtDecoder jwtDecoder, JwtUtil jwtUtil) {
        this.jwtDecoder = jwtDecoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getCredentials();
        String token = jwt.getTokenValue();  // Obtenha o valor do token como String

        logger.debug("Token: {}", token);

        if (token == null) {
            return Mono.empty();
        }

        // Processar o token
        if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            Long userId = jwtUtil.extractUserId(token);
            String email = jwtUtil.extractUsername(token);

            // Cria as autoridades
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            // Cria o JwtAuthenticationToken usando o objeto Jwt como principal
            JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, authorities);

            auth.setAuthenticated(true);
            return Mono.just(auth);
        }

        return Mono.empty();
    }

}
