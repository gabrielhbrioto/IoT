package com.iot.service;

import com.iot.model.Sala;
import com.iot.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public Mono<Sala> criarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    public Flux<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    public Mono<Sala> buscarSalaPorId(Long id) {
        return salaRepository.findById(id);
    }

    public Mono<Void> deletarSala(Long id) {
        return salaRepository.deleteById(id);
    }    
    
}
