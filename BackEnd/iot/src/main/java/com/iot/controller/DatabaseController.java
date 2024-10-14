package com.iot.controller;

import com.iot.service.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    // Injeção de dependência pelo construtor
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/test-connection")
    public Mono<ResponseEntity<String>> testDatabaseConnection() {
        return databaseService.testConnection()
                .map(connectionResult -> ResponseEntity.ok(connectionResult)) // Retorna a conexão com status 200
                .defaultIfEmpty(ResponseEntity.status(500).body("Failed to connect to the database")); // Retorno padrão em caso de erro
    }
}
