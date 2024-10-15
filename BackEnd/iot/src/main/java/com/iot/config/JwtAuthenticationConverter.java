package com.iot.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationConverter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationConverter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    // Método que converte um token JWT em um Mono<Authentication>
    public Mono<Authentication> convert(String token) {
        return Mono.fromCallable(() -> {
            Jwt decodedJwt = jwtDecoder.decode(token);
            Collection<GrantedAuthority> authorities = extractAuthorities(decodedJwt);
            String username = decodedJwt.getSubject();  // Extraímos o nome de usuário do token
            return new UsernamePasswordAuthenticationToken(username, null, authorities);  // Criamos o Authentication
        });
    }

    // Método que extrai as autoridades do JWT
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // Extraímos a claim "roles" do token
        List<String> roles = jwt.getClaimAsStringList("roles"); 
        
        // Garantimos que a lista de roles seja transformada corretamente em uma lista de SimpleGrantedAuthority
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Cria uma autoridade para cada role
                .collect(Collectors.toList());
    }
}
