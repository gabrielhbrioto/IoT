package com.iot.repository;

import com.iot.model.Usuario;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends R2dbcRepository<Usuario, Long> {
    Mono<Usuario> findByEmail(String email); // Método para buscar usuário pelo email
}
