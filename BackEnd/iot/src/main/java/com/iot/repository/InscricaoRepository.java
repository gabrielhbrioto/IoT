package com.iot.repository;
import com.iot.model.Inscricao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface InscricaoRepository extends ReactiveCrudRepository<Inscricao, Long> {
}
