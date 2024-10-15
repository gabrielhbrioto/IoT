package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;  // Injetando JwtUtil

    @PostMapping("/login")
    public Mono<String> login(@RequestParam String email, @RequestParam String senha) {
        System.out.printf("Tentativa de login com o email: %s\n", email);
        return usuarioService.buscarPorEmailESenha(email, senha)
            .flatMap(usuario -> {
                String token = jwtUtil.generateToken(usuario.getEmail());
                return Mono.just(token);
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Credenciais inválidas")));
    }
}
