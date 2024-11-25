package com.iot.controller;

import com.iot.config.JwtUtil;
import com.iot.model.Inscricao;
import com.iot.service.InscricaoService;
import com.iot.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para gerenciar inscrições.
 * 
 * Mapeia as requisições HTTP para os endpoints relacionados às inscrições.
 */
@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    /**
     * Construtor para inicializar os serviços necessários.
     * 
     * @param inscricaoService Serviço de inscrição
     * @param jwtUtil Utilitário JWT
     * @param usuarioService Serviço de usuário
     */
    public InscricaoController(InscricaoService inscricaoService, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.inscricaoService = inscricaoService;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    /**
     * Cria uma nova inscrição.
     * 
     * @param inscricao Objeto de inscrição a ser criado
     * @param authToken Token de autorização JWT
     * @return Mono<Inscricao> Inscrição criada
     */
    @PostMapping
    public Mono<Inscricao> createInscricao(@RequestBody Inscricao inscricao, 
                                           @RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        inscricao.setIdUsuario(userId);
                                        
        return inscricaoService.createInscricao(inscricao);
    }

    /**
     * Obtém todas as inscrições de um usuário.
     * 
     * @param authToken Token de autorização JWT
     * @return Flux<Inscricao> Lista de inscrições do usuário
     */
    @GetMapping("/usuario")
    public Flux<Inscricao> getInscricoesByUsuario(@RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        
        return inscricaoService.getInscricoesByUsuarioId(userId);
    }

    /**
     * Deleta uma inscrição de um usuário por ID da sala.
     * 
     * @param idSala ID da sala
     * @param authToken Token de autorização JWT
     * @return Mono<Void> Indica a conclusão da operação
     */
    @DeleteMapping("/usuario/sala/{idSala}")
    public Mono<Void> deleteInscricaoBySala(@PathVariable Long idSala,
                                            @RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        
        return inscricaoService.deleteByUserIdAndSalaId(userId, idSala);
    }
}
