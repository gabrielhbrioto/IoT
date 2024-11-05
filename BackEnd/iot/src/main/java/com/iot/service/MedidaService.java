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

    public Flux<Medida> listarMedidasPorSala(Long idSala) {
        return medidaRepository.findByIdSala(idSala);
    }

    public Flux<Medida> listarMedidasPorPeriodo(Long idSala, ZonedDateTime inicio, ZonedDateTime fim) {
        return medidaRepository.findByIdSalaAndHorarioBetween(idSala, inicio, fim);
    }

    public Flux<Medida> getAllMedidas() {
        return medidaRepository.findAll();
    }

    public Mono<Medida> getMedidaById(Long id) {
        return medidaRepository.findById(id);
    }

    public Mono<Void> deleteMedida(Long id) {
        return medidaRepository.deleteById(id);
    }
}
