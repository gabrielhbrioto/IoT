package com.iot.controller;

import com.iot.model.Medida;
import com.iot.service.MedidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

/**
 * Controlador REST para gerenciar operações relacionadas a Medidas.
 * Mapeia as requisições para "/medidas".
 */
@RestController
@RequestMapping("/medidas")
public class MedidaController {

    private final MedidaService medidaService;

    /**
     * Construtor para injetar a dependência do serviço de Medida.
     * 
     * @param medidaService Serviço de Medida
     */
    public MedidaController(MedidaService medidaService) {
        this.medidaService = medidaService;
    }

    /**
     * Endpoint para obter todas as medidas.
     * 
     * @return Fluxo de todas as medidas
     */
    @GetMapping
    public Flux<Medida> getAllMedidas() {
        return medidaService.getAllMedidas();
    }

    /**
     * Endpoint para criar uma nova medida.
     * 
     * @param medida Objeto Medida a ser criado
     * @return Mono com a resposta da entidade Medida criada
     */
    @PostMapping
    public Mono<ResponseEntity<Medida>> createMedida(@RequestBody Medida medida) {
        return medidaService.registrarMedida(medida)
                .map(novaMedida -> ResponseEntity.ok(novaMedida));
    }

    /**
     * Endpoint para obter uma medida pelo ID.
     * 
     * @param id ID da medida
     * @return Mono com a resposta da entidade Medida encontrada ou 404 se não encontrada
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Medida>> getMedidaById(@PathVariable Long id) {
        return medidaService.getMedidaById(id)
                .map(medida -> ResponseEntity.ok(medida))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar uma medida pelo ID.
     * 
     * @param id ID da medida a ser deletada
     * @return Mono com a resposta de conteúdo vazio
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMedida(@PathVariable Long id) {
        return medidaService.deleteMedida(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    /**
     * Endpoint para listar medidas por período.
     * 
     * @param idSala ID da sala
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @return Fluxo de medidas no período especificado
     */
    @GetMapping("/periodo")
    public Flux<Medida> listarMedidasPorPeriodo(
            @RequestParam("idSala") Long idSala,
            @RequestParam("inicio") ZonedDateTime inicio,
            @RequestParam("fim") ZonedDateTime fim) {
        return medidaService.listarMedidasPorPeriodo(idSala, inicio, fim);
    }

}
