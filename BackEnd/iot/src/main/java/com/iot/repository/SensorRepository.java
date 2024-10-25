package com.iot.repository;

import com.iot.model.Sensor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SensorRepository extends R2dbcRepository<Sensor, Long> {
    Flux<Sensor> findByIdSala(Long idSala); // Método para buscar sensores por sala
}
