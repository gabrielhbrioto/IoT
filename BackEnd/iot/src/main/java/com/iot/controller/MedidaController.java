package com.iot.controller;

import com.iot.model.Medida;
import com.iot.service.MedidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/medidas")
public class MedidaController {

    private final MedidaService medidaService;

    public MedidaController(MedidaService medidaService) {
        this.medidaService = medidaService;
    }

    @GetMapping
    public Flux<Medida> getAllMedidas() {
        return medidaService.getAllMedidas();
    }

    @PostMapping
    public Mono<ResponseEntity<Medida>> createMedida(@RequestBody Medida medida) {
        return medidaService.registrarMedida(medida)
                .map(novaMedida -> ResponseEntity.ok(novaMedida));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Medida>> getMedidaById(@PathVariable Long id) {
        return medidaService.getMedidaById(id)
                .map(medida -> ResponseEntity.ok(medida))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMedida(@PathVariable Long id) {
        return medidaService.deleteMedida(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
