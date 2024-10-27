package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.dto.LoginRequest;
import com.iot.dto.LoginResponse; // Importe o novo DTO
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
    public Mono<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.buscarPorEmailESenha(loginRequest.getEmail(), loginRequest.getSenha())
            .flatMap(usuario -> {
                String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getId());
                LoginResponse response = new LoginResponse(token, usuario.getId()); // Criando a resposta
                return Mono.just(response); // Retornando o objeto LoginResponse
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Credenciais inválidas")));
    }
}
