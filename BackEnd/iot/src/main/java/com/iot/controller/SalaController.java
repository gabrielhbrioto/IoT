package com.iot.controller;

import com.iot.model.Sala;
import com.iot.service.SalaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public Flux<Sala> getAllSalas() {
        return salaService.listarSalas();
    }

    @PostMapping
    public Mono<ResponseEntity<Sala>> createSala(@RequestBody Sala sala) {
        return salaService.criarSala(sala)
                .map(novaSala -> ResponseEntity.ok(novaSala));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sala>> getSalaById(@PathVariable Long id) {
        return salaService.buscarSalaPorId(id)
                .map(sala -> ResponseEntity.ok(sala))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Sala>> updateSala(@PathVariable Long id, @RequestBody Sala sala) {
        // Aqui, você precisaria implementar um método de atualização no SalaService
        return salaService.buscarSalaPorId(id)
                .flatMap(existingSala -> {
                    existingSala.setNome(sala.getNome());
                    existingSala.setCriador(sala.getCriador());
                    return salaService.criarSala(existingSala);
                })
                .map(updatedSala -> ResponseEntity.ok(updatedSala))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteSala(@PathVariable Long id) {
        return salaService.deletarSala(id)
                .then(Mono.just(ResponseEntity.noContent().build())) // Retorna um ResponseEntity<Void>
                .onErrorReturn(ResponseEntity.notFound().build()); // Retorna um ResponseEntity<Void>
    }


}
