package com.iot.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtToAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        String username = jwt.getClaimAsString("sub"); // Use o campo adequado do JWT
        Collection<SimpleGrantedAuthority> authorities = extractAuthorities(jwt);

        // Cria o token de autenticação com o usuário e suas authorities
        return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        // Extrai authorities do JWT, assumindo que estão no claim "roles" como uma lista
        return jwt.getClaimAsStringList("roles")
                  .stream()
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toList());
    }
}
