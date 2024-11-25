package com.iot.service;

import com.iot.model.Medida;
import com.iot.repository.MedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.ZonedDateTime;


/**
 * Serviço para gerenciar operações relacionadas a Medida.
 */
@Service
public class MedidaService {

    @Autowired
    private MedidaRepository medidaRepository;

    /**
     * Registra uma nova medida.
     * 
     * @param medida a medida a ser registrada
     * @return a medida registrada
     */
    public Mono<Medida> registrarMedida(Medida medida) {
        return medidaRepository.save(medida);
    }

    /**
     * Lista todas as medidas de uma sala específica.
     * 
     * @param idSala o ID da sala
     * @return um fluxo de medidas da sala
     */
    public Flux<Medida> listarMedidasPorSala(Long idSala) {
        return medidaRepository.findByIdSala(idSala);
    }

    /**
     * Lista todas as medidas de uma sala em um período específico.
     * 
     * @param idSala o ID da sala
     * @param inicio o início do período
     * @param fim o fim do período
     * @return um fluxo de medidas da sala no período especificado
     */
    public Flux<Medida> listarMedidasPorPeriodo(Long idSala, ZonedDateTime inicio, ZonedDateTime fim) {
        return medidaRepository.findByIdSalaAndHorarioBetween(idSala, inicio, fim);
    }

    /**
     * Lista todas as medidas.
     * 
     * @return um fluxo de todas as medidas
     */
    public Flux<Medida> getAllMedidas() {
        return medidaRepository.findAll();
    }

    /**
     * Obtém uma medida pelo seu ID.
     * 
     * @param id o ID da medida
     * @return a medida correspondente ao ID
     */
    public Mono<Medida> getMedidaById(Long id) {
        return medidaRepository.findById(id);
    }

    /**
     * Deleta uma medida pelo seu ID.
     * 
     * @param id o ID da medida a ser deletada
     * @return um Mono vazio após a exclusão
     */
    public Mono<Void> deleteMedida(Long id) {
        return medidaRepository.deleteById(id);
    }
}
