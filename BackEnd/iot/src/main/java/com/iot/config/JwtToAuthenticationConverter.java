package com.iot.config;

import com.iot.model.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtToAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final JwtUtil jwtUtil;

    // Injeção de dependência do JwtUtil
    public JwtToAuthenticationConverter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        // Extraindo o token JWT como uma string
        String token = jwt.getTokenValue();

        // Usando JwtUtil para extrair as informações do token
        Long userId = jwtUtil.extractUserId(token);
        String email = jwtUtil.extractUsername(token);

        // Criar uma lista vazia de authorities, já que você não está usando roles
        return Mono.just(new UsernamePasswordAuthenticationToken(
            new UserPrincipal(userId, email),  // Cria um principal personalizado
            null,
            Collections.emptyList()            // Lista vazia de authorities
        ));
    }
}
