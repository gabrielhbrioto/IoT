package com.iot.repository;

import com.iot.model.Sala;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SalaRepository extends ReactiveCrudRepository<Sala, Long> {
}
