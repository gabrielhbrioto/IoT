package com.iot.repository;

import com.iot.model.Medida;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;

public interface MedidaRepository extends R2dbcRepository<Medida, Long> {
    // Método para encontrar medidas por ID do sensor
    Flux<Medida> findByIdSensor(Long idSensor);

    // Método para encontrar medidas por ID do sensor em um período
    Flux<Medida> findByIdSensorAndHorarioBetween(Long idSensor, ZonedDateTime inicio, ZonedDateTime fim);
}
