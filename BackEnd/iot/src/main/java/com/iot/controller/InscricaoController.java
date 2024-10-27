package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.model.Inscricao;
import com.iot.service.InscricaoService;
import com.iot.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping
    public Mono<Inscricao> createInscricao(@RequestBody Inscricao inscricao, 
                                           @RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token); // Extrai o ID do usuário do token JWT
        inscricao.setIdUsuario(userId); // Define o ID do usuário no objeto inscrição
                                        
        return inscricaoService.createInscricao(inscricao);
    }

    @GetMapping("/usuario")
    public Flux<Inscricao> getInscricoesByUsuario(@RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token); // Extrai o ID do usuário do token JWT
        
        return inscricaoService.getInscricoesByUsuarioId(userId);
    }

    // Novo endpoint para cancelar uma inscrição de uma sala específica
    @DeleteMapping("/usuario/sala/{idSala}")
    public Mono<Void> deleteInscricaoBySala(@PathVariable Long idSala,
                                            @RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token); // Extrai o ID do usuário do token JWT
        
        // Chama o serviço para excluir a inscrição com o ID do usuário e o ID da sala
        return inscricaoService.deleteByUserIdAndSalaId(userId, idSala);
    }
}
