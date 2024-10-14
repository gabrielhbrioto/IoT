package com.iot.repository;

import com.iot.model.Sensor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SensorRepository extends ReactiveCrudRepository<Sensor, Long> {
    Flux<Sensor> findBySalaId(Long salaId); // Método para buscar sensores por sala
}
