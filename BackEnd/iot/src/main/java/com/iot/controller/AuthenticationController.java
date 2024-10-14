package com.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private ReactiveUserDetailsService userDetailsService; // Alterado para ReactiveUserDetailsService

    @PostMapping("/login")
    public Mono<UserDetails> login(@RequestParam String email, @RequestParam String senha) {
        return userDetailsService.findByUsername(email)
            .flatMap(userDetails -> {
                // Adicione aqui sua lógica de autenticação, por exemplo, verificar a senha
                if (userDetails.getPassword().equals(senha)) {
                    return Mono.just(userDetails);
                }
                return Mono.error(new RuntimeException("Senha incorreta"));
            });
    }
}
