package com.iot.controller;

import com.iot.model.Sensor;
import com.iot.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sensores")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public Flux<Sensor> getAllSensores() {
        return sensorService.listarSensoresPorSala(null); // Ajustar conforme a lógica
    }

    @PostMapping
    public Mono<ResponseEntity<Sensor>> createSensor(@RequestBody Sensor sensor) {
        return sensorService.criarSensor(sensor)
                .map(novoSensor -> ResponseEntity.ok(novoSensor));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sensor>> getSensorById(@PathVariable Long id) {
        return sensorService.buscarSensorPorId(id)
                .map(sensor -> ResponseEntity.ok(sensor))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Sensor>> updateSensor(@PathVariable Long id, @RequestBody Sensor sensor) {
        // Você precisaria implementar a lógica de atualização no service
        return sensorService.buscarSensorPorId(id)
                .flatMap(existingSensor -> {
                    existingSensor.setTipo(sensor.getTipo());
                    existingSensor.setSala(sensor.getSala());
                    return sensorService.criarSensor(existingSensor);
                })
                .map(updatedSensor -> ResponseEntity.ok(updatedSensor))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteSensor(@PathVariable Long id) {
        return sensorService.deletarSensor(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
