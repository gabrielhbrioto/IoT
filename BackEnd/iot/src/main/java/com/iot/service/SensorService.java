package com.iot.service;

import com.iot.model.Sensor;
import com.iot.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

/**
 * Serviço responsável por operações relacionadas a sensores.
 */
@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    /**
     * Cria um novo sensor.
     *
     * @param sensor Objeto do sensor a ser criado.
     * @return Mono contendo o sensor criado.
     */
    public Mono<Sensor> criarSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    /**
     * Lista todos os sensores de uma sala específica.
     *
     * @param idSala ID da sala.
     * @return Flux contendo os sensores da sala.
     */
    public Flux<Sensor> listarSensoresPorSala(Long idSala) {
        return sensorRepository.findByIdSala(idSala);
    }

    /**
     * Busca um sensor pelo seu ID.
     *
     * @param id ID do sensor.
     * @return Mono contendo o sensor encontrado.
     */
    public Mono<Sensor> buscarSensorPorId(Long id) {
        return sensorRepository.findById(id);
    }

    /**
     * Deleta um sensor pelo seu ID.
     *
     * @param id ID do sensor a ser deletado.
     * @return Mono<Void> indicando a conclusão da operação.
     */
    public Mono<Void> deletarSensor(Long id) {
        return sensorRepository.deleteById(id);
    }
}
