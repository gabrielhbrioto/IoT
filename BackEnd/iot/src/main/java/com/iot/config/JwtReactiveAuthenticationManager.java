package com.iot.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importação correta
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public JwtReactiveAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        
        // Validar o token JWT
        if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            // Extrair informações do token
            //String username = jwtUtil.extractUsername(token); // Corrigido
            Long userId = jwtUtil.extractUserId(token);
            
            // Criar um novo token de autenticação com o usuário e suas permissões
            AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, token); // Correção para UsernamePasswordAuthenticationToken
            auth.setAuthenticated(true);
            return Mono.just(auth);
        }
        
        return Mono.empty();
    }
}
