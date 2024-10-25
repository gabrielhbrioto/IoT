package com.iot.controller;

import com.iot.model.Inscricao;
import com.iot.model.Sala;
import com.iot.model.Sensor;
import com.iot.service.SalaService;
import com.iot.service.SensorService; 
import com.iot.service.InscricaoService; 
import com.iot.service.UsuarioService;
import com.iot.config.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;
    private final SensorService sensorService; 
    private final InscricaoService inscricaoService; 
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public SalaController(SalaService salaService, SensorService sensorService, JwtUtil jwtUtil, UsuarioService usuarioService, InscricaoService inscricaoService) { // Ajustar o construtor
        this.salaService = salaService;
        this.sensorService = sensorService; // Ajustar aqui
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.inscricaoService = inscricaoService;
    }

    @GetMapping
    public Flux<Sala> getAllSalas() {
        return salaService.listarSalas();
    }

    @PostMapping
    public Mono<ResponseEntity<Sala>> createSala(@RequestBody Sala sala, @RequestHeader("Authorization") String authToken) {
        // Remove o prefixo "Bearer " do token e extrai as claims
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token); // Extrai o ID do usuário do token

        return usuarioService.buscarPorId(userId)
            .flatMap(usuario -> {
                sala.setIdCriador(userId);  // Define apenas o ID do criador
                return salaService.criarSala(sala)
                    .flatMap(novaSala -> {
                        // Após a criação da sala, crie três sensores (presença, tensão e corrente)
                        Sensor sensorPresenca = new Sensor();
                        sensorPresenca.setIdSala(novaSala.getId()); 
                        sensorPresenca.setTipo("PRESENCA");

                        Sensor sensorTensao = new Sensor();
                        sensorTensao.setIdSala(novaSala.getId()); 
                        sensorTensao.setTipo("TENSAO");

                        Sensor sensorCorrente = new Sensor();
                        sensorCorrente.setIdSala(novaSala.getId()); 
                        sensorCorrente.setTipo("CORRENTE");

                        Inscricao inscricao = new Inscricao();
                        inscricao.setIdSala(novaSala.getId());
                        inscricao.setIdUsuario(userId);

                        return Flux.concat(
                                sensorService.criarSensor(sensorPresenca),
                                sensorService.criarSensor(sensorTensao),
                                sensorService.criarSensor(sensorCorrente),
                                inscricaoService.createInscricao(inscricao)
                            )
                            .then(Mono.just(ResponseEntity.ok(novaSala)));  // Retorna a sala após criar os sensores
                    });
            });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sala>> getSalaById(@PathVariable Long id) {
        return salaService.buscarSalaPorId(id)
                .map(sala -> ResponseEntity.ok(sala))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Sala>> updateSala(@PathVariable Long id, @RequestBody Sala sala) {
        // Aqui, você precisaria implementar um método de atualização no SalaService
        return salaService.buscarSalaPorId(id)
                .flatMap(existingSala -> {
                    existingSala.setNome(sala.getNome());
                    existingSala.setIdCriador(sala.getIdCriador());
                    return salaService.criarSala(existingSala);
                })
                .map(updatedSala -> ResponseEntity.ok(updatedSala))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteSala(@PathVariable Long id) {
        return salaService.deletarSala(id)
                .then(Mono.just(ResponseEntity.noContent().build())) // Retorna um ResponseEntity<Void>
                .onErrorReturn(ResponseEntity.notFound().build()); // Retorna um ResponseEntity<Void>
    }
}
