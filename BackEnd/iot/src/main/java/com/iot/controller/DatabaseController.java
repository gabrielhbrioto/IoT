package com.iot.controller;

import com.iot.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para operações relacionadas ao banco de dados.
 */
@RestController
@RequestMapping("/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    /**
     * Construtor que injeta o serviço de banco de dados.
     *
     * @param databaseService Serviço de banco de dados.
     */
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Endpoint para testar a conexão com o banco de dados.
     *
     * @return Mono contendo a resposta da entidade com o resultado da conexão.
     */
    @GetMapping("/test-connection")
    public Mono<ResponseEntity<String>> testDatabaseConnection() {
        return databaseService.testConnection()
                .map(connectionResult -> ResponseEntity.ok(connectionResult))
                .defaultIfEmpty(ResponseEntity.status(500).body("Failed to connect to the database"));
    }
}
