package com.iot.controller;

import com.iot.model.Inscricao;
import com.iot.service.InscricaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @GetMapping
    public Flux<Inscricao> getAllInscricoes() {
        return inscricaoService.getAllInscricoes(); // Retorna Flux de Inscrições
    }

    @PostMapping
    public Mono<ResponseEntity<Inscricao>> createInscricao(@RequestBody Inscricao inscricao) {
        return inscricaoService.createInscricao(inscricao)
                .map(novaInscricao -> ResponseEntity.ok(novaInscricao)); // Retorna a nova inscrição
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInscricao(@PathVariable Long id) {
        return inscricaoService.deleteInscricao(id)
                .then(Mono.just(ResponseEntity.noContent().build())); // Retorna resposta sem conteúdo
    }
}
