package com.iot.repository;

import com.iot.model.Inscricao;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface InscricaoRepository extends R2dbcRepository<Inscricao, Long> {
    Flux<Inscricao> findByIdUsuario(Long idUsuario); // Busca inscrições por ID do usuário

    Mono<Void> deleteByIdUsuarioAndIdSala(Long idUsuario, Long idSala);
    Mono<Void> deleteByIdSala(Long idSala); // Exclui inscrições por ID da sala
}
