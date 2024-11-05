package com.iot.repository;

import com.iot.model.Medida;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import java.time.ZonedDateTime;

public interface MedidaRepository extends R2dbcRepository<Medida, Long> {
    // Método para encontrar medidas por ID da sala
    Flux<Medida> findByIdSala(Long idSala);

    // Método para encontrar medidas por ID da sala em um período
    Flux<Medida> findByIdSalaAndHorarioBetween(Long idSala, ZonedDateTime inicio, ZonedDateTime fim);
}
