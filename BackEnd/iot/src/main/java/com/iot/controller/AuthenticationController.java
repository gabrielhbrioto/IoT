package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.dto.LoginRequest;
import com.iot.dto.LoginResponse;
import com.iot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

/**
 * Controlador responsável pela autenticação de usuários.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para login de usuários.
     * 
     * @param loginRequest Objeto contendo email e senha do usuário.
     * @return Mono<LoginResponse> contendo o token JWT e o ID do usuário.
     */
    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.buscarPorEmailESenha(loginRequest.getEmail(), loginRequest.getSenha())
            .flatMap(usuario -> {
                String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getId());
                LoginResponse response = new LoginResponse(token, usuario.getId());
                return Mono.just(response);
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Credenciais inválidas")));
    }
}
