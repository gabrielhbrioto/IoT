package com.iot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.iot.repository.UsuarioRepository; // Importe seu repositório de usuários
import com.iot.model.Usuario; // Importe sua classe de modelo de usuário
import org.springframework.security.core.userdetails.User; 
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UsuarioRepository userRepository; // Seu repositório de usuários

    @Override
    public Mono<UserDetails> findByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuário não encontrado: " + email)))
            .map(this::mapToUserDetails);
    }

    private UserDetails mapToUserDetails(Usuario usuario) {
        // Aqui você deve criar uma lista de autoridades com base no seu modelo de usuário
        return new User(usuario.getEmail(), usuario.getSenha(), Collections.singletonList(new SimpleGrantedAuthority("USER"))); // Substitua "USER" pela autoridade real do seu sistema
    }
}
