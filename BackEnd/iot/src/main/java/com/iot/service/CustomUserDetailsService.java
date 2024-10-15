package com.iot.service;

import com.iot.repository.UsuarioRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return usuarioRepository.findByEmail(email)
            .map(usuario -> {
                // Aqui você cria um UserDetails a partir do seu objeto Usuario
                return org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getSenha())
                        .roles("USER") // ou outros papéis conforme necessário
                        .build();
            })
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuário não encontrado com o email: " + email)));
    }
}
