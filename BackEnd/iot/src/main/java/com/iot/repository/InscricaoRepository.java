package com.iot.repository;

import com.iot.model.Inscricao;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface InscricaoRepository extends R2dbcRepository<Inscricao, Long> {
}
