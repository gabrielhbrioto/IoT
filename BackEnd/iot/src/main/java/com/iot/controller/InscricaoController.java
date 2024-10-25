package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.model.Inscricao;
import com.iot.service.InscricaoService;
import com.iot.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public InscricaoController(InscricaoService inscricaoService, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.inscricaoService = inscricaoService;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuario")
    public Flux<Inscricao> getInscricoesByUsuario(@RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token); // Extrai o ID do usuário do token JWT
        
        return inscricaoService.getInscricoesByUsuarioId(userId);
    }
}
