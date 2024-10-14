package com.iot.repository;

import com.iot.model.Medida;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;

public interface MedidaRepository extends ReactiveCrudRepository<Medida, Long> {
    // Método para encontrar medidas por sensor
    Flux<Medida> findBySensorId(Long sensorId);

    // Método para encontrar medidas por sensor em um período
    Flux<Medida> findBySensorIdAndHorarioBetween(Long sensorId, ZonedDateTime inicio, ZonedDateTime fim);
}
