package com.iot.service;

import com.iot.model.Medida;
import com.iot.repository.MedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.ZonedDateTime;

//apagar:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MedidaService {

    @Autowired
    private MedidaRepository medidaRepository;

    //apagar:
    private static final Logger logger = LoggerFactory.getLogger(MedidaService.class);


    public Mono<Medida> registrarMedida(Medida medida) {
        return medidaRepository.save(medida);
    }

    public Flux<Medida> listarMedidasPorSala(Long idSala) {
        return medidaRepository.findByIdSala(idSala);
    }

    public Flux<Medida> listarMedidasPorPeriodo(Long idSala, ZonedDateTime inicio, ZonedDateTime fim) {
        //apagar:
        logger.info("Consulta SQL - Parâmetros: idSala={}, inicio={}, fim={}", idSala, inicio, fim);
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
