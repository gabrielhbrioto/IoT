package com.iot.controller;

import com.iot.model.Inscricao;
import com.iot.model.Sala;
import com.iot.model.Sensor;
import com.iot.repository.InscricaoRepository;
import com.iot.repository.SalaRepository;
import com.iot.service.SalaService;
import com.iot.service.SensorService; 
import com.iot.service.InscricaoService; 
import com.iot.service.UsuarioService;
import com.iot.config.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/salas")
public class SalaController {


    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

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
    public Mono<ResponseEntity<Map<String, Object>>> createSala(@RequestBody Sala sala, @RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token); // Extrai o ID do usuário do token
    
        return usuarioService.buscarPorId(userId)
            .flatMap(usuario -> {
                sala.setIdCriador(userId);  // Define apenas o ID do criador
                return salaService.criarSala(sala)
                    .flatMap(novaSala -> {
                        // Criação dos sensores
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
                                sensorService.criarSensor(sensorCorrente)
                            )
                            .collectList() // Coleta todos os sensores criados em uma lista
                            .flatMap(sensoresCriados -> 
                                inscricaoService.createInscricao(inscricao)
                                    .map(inscricaoCriada -> {
                                        // Prepara a resposta com a sala, os sensores criados e a inscrição separada
                                        Map<String, Object> response = new HashMap<>();
                                        response.put("sala", novaSala);
                                        response.put("sensores", sensoresCriados);
                                        response.put("inscricao", inscricaoCriada);
                                        return ResponseEntity.ok(response); // Retorna a sala, sensores e inscrição
                                    })
                            );
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
    public Mono<ResponseEntity<String>> deleteSala(@PathVariable Long id) {
        return salaRepository.findById(id)
            .flatMap(sala -> salaRepository.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("Sala excluída com sucesso"))))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar a sala: " + e.getMessage())));
    }


}
