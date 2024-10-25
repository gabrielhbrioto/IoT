package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.dto.LoginRequest;
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
    public Mono<String> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.buscarPorEmailESenha(loginRequest.getEmail(), loginRequest.getSenha())
            .flatMap(usuario -> {
                String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getId()); 
                return Mono.just(token);
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Credenciais inválidas")));
    }

}
