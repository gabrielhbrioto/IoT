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

@Component
public class JwtToAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final JwtUtil jwtUtil;

    // Injeção de dependência do JwtUtil
    public JwtToAuthenticationConverter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        String token = jwt.getTokenValue();
    
        if (token == null) {
            return Mono.empty();
        }

        // Extrai o ID do usuário e o e-mail do token JWT
        Long userId = jwtUtil.extractUserId(token);
        String email = jwtUtil.extractUsername(token);
    
        // Cria uma lista de authorities com a role "ROLE_USER"
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    
        // Cria um UserPrincipal que pode ser utilizado como "principal" na autenticação
        UserPrincipal userPrincipal = new UserPrincipal(userId, email);
    
        // Cria um JwtAuthenticationToken utilizando o objeto JWT decodificado e as authorities
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, authorities);
    
        // Define o principal como UserPrincipal
        authenticationToken.setDetails(userPrincipal);
    
        // Retorne o JwtAuthenticationToken como parte do fluxo reativo
        return Mono.just(authenticationToken);
    }
}
