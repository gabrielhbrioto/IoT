package com.iot.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Classe de serviço responsável por operações relacionadas ao banco de dados.
 */
@Service
public class DatabaseService {

    /**
     * Testa a conexão com o banco de dados.
     *
     * @return Mono<String> contendo uma mensagem de sucesso se a conexão for bem-sucedida, ou um Mono vazio se falhar.
     */
    public Mono<String> testConnection() {
        boolean isConnected = true; // substituir pela lógica real de conexão

        if (isConnected) {
            return Mono.just("Conexão com o banco de dados bem-sucedida!");
        } else {
            return Mono.empty();
        }
    }
}
