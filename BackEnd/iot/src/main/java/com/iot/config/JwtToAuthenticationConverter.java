package com.iot.config;

import com.iot.model.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * JwtToAuthenticationConverter é um componente que implementa a interface Converter para converter um objeto Jwt em um Mono<AbstractAuthenticationToken>.
 * 
 * Este conversor utiliza JwtUtil para extrair informações do token JWT e criar um objeto JwtAuthenticationToken com essas informações.
 */
@Component
public class JwtToAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final JwtUtil jwtUtil;

    public JwtToAuthenticationConverter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        String token = jwt.getTokenValue();
    
        if (token == null) {
            return Mono.empty();
        }

        Long userId = jwtUtil.extractUserId(token);
        String email = jwtUtil.extractUsername(token);
    
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    
        UserPrincipal userPrincipal = new UserPrincipal(userId, email);
    
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, authorities);
    
        authenticationToken.setDetails(userPrincipal);
    
        return Mono.just(authenticationToken);
    }
}
