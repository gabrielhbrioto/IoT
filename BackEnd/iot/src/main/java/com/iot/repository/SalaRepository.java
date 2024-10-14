package com.iot.repository;

import com.iot.model.Sala;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SalaRepository extends R2dbcRepository<Sala, Long> {
}
