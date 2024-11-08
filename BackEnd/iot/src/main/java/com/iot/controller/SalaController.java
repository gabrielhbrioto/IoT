package com.iot.controller;

import com.iot.model.Inscricao;
import com.iot.model.Sala;
import com.iot.model.Sensor;
import com.iot.repository.InscricaoRepository;
import com.iot.repository.SalaRepository;
import com.iot.service.SalaService;
import com.iot.service.SensorService; 
import com.iot.service.InscricaoService;
import com.iot.service.MedidaService;
import com.iot.service.UsuarioService;
import com.iot.config.JwtUtil;
import com.iot.service.MqttService;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//apagar:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/salas")
public class SalaController {


    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    //apagar:
    private static final Logger logger = LoggerFactory.getLogger(MedidaService.class);

    private final MqttService mqttService;

    private final SalaService salaService;
    private final SensorService sensorService; 
    private final InscricaoService inscricaoService; 
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public SalaController(SalaService salaService, SensorService sensorService, JwtUtil jwtUtil, UsuarioService usuarioService, InscricaoService inscricaoService, MqttService mqttService) { 
        this.salaService = salaService;
        this.sensorService = sensorService; 
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.inscricaoService = inscricaoService;
        this.mqttService = mqttService;
    }

    @GetMapping
    public Flux<Sala> getAllSalas() {
        return salaService.listarSalas();
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createSala(@RequestBody Sala sala, @RequestHeader("Authorization") String authToken) {
        String token = authToken.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        return usuarioService.buscarPorId(userId)
            .flatMap(usuario -> {
                sala.setIdCriador(userId);

                return salaService.criarSala(sala)
                    .flatMap(novaSala -> {
                        String topic = novaSala.getId() + "/medidas";
                        //apagar:
                        logger.info("Tópico MQTT: {}", topic);
  
                        try {
                            mqttService.subscribe(topic); // Tenta se inscrever no tópico
                        } catch (MqttException e) {
                            // Inscrição falhou, então a sala é removida
                            return salaService.deletarSalaComInscricoes(novaSala.getId())
                                .then(Mono.error(new RuntimeException("Erro ao se inscrever no tópico MQTT: " + e.getMessage())));
                        }

                        // Se a inscrição for bem-sucedida, continue com a criação dos sensores e inscrição
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
                            .collectList()
                            .flatMap(sensoresCriados -> 
                                inscricaoService.createInscricao(inscricao)
                                    .map(inscricaoCriada -> {
                                        Map<String, Object> response = new HashMap<>();
                                        response.put("sala", novaSala);
                                        response.put("sensores", sensoresCriados);
                                        response.put("inscricao", inscricaoCriada);
                                        return ResponseEntity.ok(response);
                                    })
                            );
                    });
            })
            .onErrorResume(e -> {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
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
