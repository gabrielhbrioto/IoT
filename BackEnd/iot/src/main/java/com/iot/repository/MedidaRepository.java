package com.iot.repository;

import com.iot.model.Medida;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;

public interface MedidaRepository extends R2dbcRepository<Medida, Long> {
    // Método para encontrar medidas por sensor
    Flux<Medida> findBySensorId(Long sensorId);

    // Método para encontrar medidas por sensor em um período
    Flux<Medida> findBySensorIdAndHorarioBetween(Long sensorId, ZonedDateTime inicio, ZonedDateTime fim);
}
