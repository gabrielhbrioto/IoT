package com.iot.controller;

import com.iot.model.Sensor;
import com.iot.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para gerenciar sensores.
 * Mapeia as requisições HTTP para operações CRUD em sensores.
 */
@RestController
@RequestMapping("/sensores")
public class SensorController {

    private final SensorService sensorService;

    /**
     * Construtor para injetar a dependência do serviço de sensores.
     * 
     * @param sensorService serviço de sensores
     */
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * Endpoint para listar todos os sensores.
     * 
     * @return Flux<Sensor> fluxo de sensores
     */
    @GetMapping
    public Flux<Sensor> getAllSensores() {
        return sensorService.listarSensoresPorSala(null); 
    }

    /**
     * Endpoint para criar um novo sensor.
     * 
     * @param sensor objeto Sensor a ser criado
     * @return Mono<ResponseEntity<Sensor>> resposta com o sensor criado
     */
    @PostMapping
    public Mono<ResponseEntity<Sensor>> createSensor(@RequestBody Sensor sensor) {
        return sensorService.criarSensor(sensor)
                .map(novoSensor -> ResponseEntity.ok(novoSensor));
    }

    /**
     * Endpoint para buscar um sensor pelo ID.
     * 
     * @param id identificador do sensor
     * @return Mono<ResponseEntity<Sensor>> resposta com o sensor encontrado ou 404 se não encontrado
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sensor>> getSensorById(@PathVariable Long id) {
        return sensorService.buscarSensorPorId(id)
                .map(sensor -> ResponseEntity.ok(sensor))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para atualizar um sensor existente.
     * 
     * @param id identificador do sensor a ser atualizado
     * @param sensor objeto Sensor com os novos dados
     * @return Mono<ResponseEntity<Sensor>> resposta com o sensor atualizado ou 404 se não encontrado
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Sensor>> updateSensor(@PathVariable Long id, @RequestBody Sensor sensor) {
        return sensorService.buscarSensorPorId(id)
                .flatMap(existingSensor -> {
                    existingSensor.setTipo(sensor.getTipo());
                    existingSensor.setIdSala(sensor.getIdSala());
                    return sensorService.criarSensor(existingSensor);
                })
                .map(updatedSensor -> ResponseEntity.ok(updatedSensor))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar um sensor pelo ID.
     * 
     * @param id identificador do sensor a ser deletado
     * @return Mono<ResponseEntity<Object>> resposta com status 204 se deletado ou 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteSensor(@PathVariable Long id) {
        return sensorService.deletarSensor(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
