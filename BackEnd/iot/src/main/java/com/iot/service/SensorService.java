package com.iot.service;

import com.iot.model.Sensor;
import com.iot.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public Mono<Sensor> criarSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Flux<Sensor> listarSensoresPorSala(Long salaId) {
        return sensorRepository.findBySalaId(salaId);
    }

    public Mono<Sensor> buscarSensorPorId(Long id) {
        return sensorRepository.findById(id);
    }

    public Mono<Void> deletarSensor(Long id) {
        return sensorRepository.deleteById(id);
    }
}
