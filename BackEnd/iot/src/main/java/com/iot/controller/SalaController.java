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

/**
 * Controlador REST para gerenciar operações relacionadas a Salas.
 */
@RestController
@RequestMapping("/salas")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    private final MqttService mqttService;

    private final SalaService salaService;
    private final SensorService sensorService; 
    private final InscricaoService inscricaoService; 
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    /**
     * Construtor para injeção de dependências.
     */
    public SalaController(SalaService salaService, SensorService sensorService, JwtUtil jwtUtil, UsuarioService usuarioService, InscricaoService inscricaoService, MqttService mqttService) { 
        this.salaService = salaService;
        this.sensorService = sensorService; 
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.inscricaoService = inscricaoService;
        this.mqttService = mqttService;
    }

    /**
     * Obtém todas as salas.
     * @return Flux<Sala> - fluxo de salas.
     */
    @GetMapping
    public Flux<Sala> getAllSalas() {
        return salaService.listarSalas();
    }

    /**
     * Cria uma nova sala.
     * @param sala - objeto Sala a ser criado.
     * @param authToken - token de autenticação.
     * @return Mono<ResponseEntity<Map<String, Object>>> - resposta contendo a nova sala, sensores e inscrição.
     */
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
  
                        try {
                            mqttService.subscribe(topic);
                        } catch (MqttException e) {
                            return salaService.deletarSalaComInscricoes(novaSala.getId())
                                .then(Mono.error(new RuntimeException("Erro ao se inscrever no tópico MQTT: " + e.getMessage())));
                        }

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

    /**
     * Obtém uma sala pelo ID.
     * @param id - ID da sala.
     * @return Mono<ResponseEntity<Sala>> - resposta contendo a sala encontrada.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Sala>> getSalaById(@PathVariable Long id) {
        return salaService.buscarSalaPorId(id)
                .map(sala -> ResponseEntity.ok(sala))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza uma sala existente.
     * @param id - ID da sala a ser atualizada.
     * @param sala - objeto Sala com os novos dados.
     * @return Mono<ResponseEntity<Sala>> - resposta contendo a sala atualizada.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Sala>> updateSala(@PathVariable Long id, @RequestBody Sala sala) {
        return salaService.buscarSalaPorId(id)
                .flatMap(existingSala -> {
                    existingSala.setNome(sala.getNome());
                    existingSala.setIdCriador(sala.getIdCriador());
                    return salaService.criarSala(existingSala);
                })
                .map(updatedSala -> ResponseEntity.ok(updatedSala))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Deleta uma sala pelo ID.
     * @param id - ID da sala a ser deletada.
     * @return Mono<ResponseEntity<String>> - resposta indicando o sucesso ou falha da operação.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteSala(@PathVariable Long id) {
        return salaRepository.findById(id)
            .flatMap(sala -> salaRepository.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("Sala excluída com sucesso"))))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar a sala: " + e.getMessage())));
    }

    /**
     * Atualiza o estado de uma sala e envia uma mensagem MQTT.
     * @param id - ID da sala.
     * @param request - mapa contendo a mensagem com o novo estado.
     * @return Mono<ResponseEntity<String>> - resposta indicando o sucesso ou falha da operação.
     */
    @PostMapping("/{id}/estado")
    public Mono<ResponseEntity<String>> atualizarEstadoEEnviarMensagemMqtt(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String novoEstado = request.get("mensagem");
        if (!"acender".equalsIgnoreCase(novoEstado) && !"automatico".equalsIgnoreCase(novoEstado) && !"apagar".equalsIgnoreCase(novoEstado)) {
            return Mono.just(ResponseEntity.badRequest().body("Estado inválido. Os estados permitidos são: 'acender', 'automatico', 'apagar'."));
        }
    
        return salaService.buscarSalaPorId(id)
                .flatMap(sala -> {
                    sala.setEstado(novoEstado);
                    
                    return salaService.criarSala(sala)
                            .flatMap(salaAtualizada -> {
                                try {
                                    mqttService.publish(id + "/luzes", novoEstado.toLowerCase());
                                    return Mono.just(ResponseEntity.ok("Estado atualizado e mensagem enviada para o tópico: " + novoEstado));
                                } catch (Exception e) {
                                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar mensagem: " + e.getMessage()));
                                }
                            });
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Obtém o estado atual de uma sala.
     * @param id - ID da sala.
     * @return Mono<ResponseEntity<String>> - resposta contendo o estado da sala.
     */
    @GetMapping("/{id}/estado")
    public Mono<ResponseEntity<String>> obterEstadoSala(@PathVariable Long id) {
        return salaService.buscarSalaPorId(id)
                .map(sala -> ResponseEntity.ok(sala.getEstado()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
