package com.iot.service;

import com.iot.model.Medida;
import com.iot.repository.MedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
public class MedidaService {

    @Autowired
    private MedidaRepository medidaRepository;

    public Mono<Medida> registrarMedida(Medida medida) {
        return medidaRepository.save(medida);
    }

    public Flux<Medida> listarMedidasPorSensor(Long idSensor) {
        return medidaRepository.findByIdSensor(idSensor);
    }

    public Flux<Medida> listarMedidasPorPeriodo(Long idSensor, ZonedDateTime inicio, ZonedDateTime fim) {
        return medidaRepository.findByIdSensorAndHorarioBetween(idSensor, inicio, fim);
    }

    public Flux<Medida> getAllMedidas() {
        return medidaRepository.findAll(); // Método para buscar todas as medidas
    }

    public Mono<Medida> getMedidaById(Long id) {
        return medidaRepository.findById(id); // Método para buscar medida pelo ID
    }

    public Mono<Void> deleteMedida(Long id) {
        return medidaRepository.deleteById(id); // Método para deletar a medida pelo ID
    }
}
