package com.iot.service;

import com.iot.model.Sala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.iot.repository.InscricaoRepository;
import com.iot.repository.SalaRepository;


@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    public Mono<Sala> criarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    public Flux<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    public Mono<Sala> buscarSalaPorId(Long id) {
        return salaRepository.findById(id);
    }

    public Mono<Void> deletarSalaComInscricoes(Long id) {
        return inscricaoRepository.deleteByIdSala(id)
            .then(salaRepository.deleteById(id));
    }    
}
