package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.model.Inscricao;
import com.iot.service.InscricaoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.iot.service.UsuarioService;
import com.iot.config.JwtUtil;

//apagar:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    //apagar
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public InscricaoController(InscricaoService inscricaoService, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.inscricaoService = inscricaoService;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public Flux<Inscricao> getAllInscricoes() {
        return inscricaoService.getAllInscricoes(); // Retorna Flux de Inscrições
    }

    @PostMapping
    public Mono<ResponseEntity<Inscricao>> createInscricao(
            @RequestBody Inscricao inscricao, 
            @RequestHeader("Authorization") String authToken) {
            
        // Remove o prefixo "Bearer " do token e extrai o ID do usuário
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        return usuarioService.buscarPorId(userId)
            .flatMap(usuario -> {
                inscricao.setIdUsuario(userId);  // Define o ID do usuário na inscrição
                return inscricaoService.createInscricao(inscricao)
                        .map(novaInscricao -> ResponseEntity.ok(novaInscricao)); // Retorna a nova inscrição
            })
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInscricao(@PathVariable Long id) {
        return inscricaoService.deleteInscricao(id)
                .then(Mono.just(ResponseEntity.noContent().build())); // Retorna resposta sem conteúdo
    }
}
