package com.iot.service;

import com.iot.model.Sala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.iot.repository.InscricaoRepository;
import com.iot.repository.SalaRepository;


/**
 * Serviço responsável por gerenciar operações relacionadas a Sala.
 */
@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    /**
     * Cria uma nova sala.
     *
     * @param sala Objeto Sala a ser criado.
     * @return Mono<Sala> Sala criada.
     */
    public Mono<Sala> criarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    /**
     * Lista todas as salas.
     *
     * @return Flux<Sala> Lista de todas as salas.
     */
    public Flux<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    /**
     * Busca uma sala pelo seu ID.
     *
     * @param id ID da sala a ser buscada.
     * @return Mono<Sala> Sala encontrada.
     */
    public Mono<Sala> buscarSalaPorId(Long id) {
        return salaRepository.findById(id);
    }

    /**
     * Deleta uma sala e suas inscrições associadas.
     *
     * @param id ID da sala a ser deletada.
     * @return Mono<Void> Indica a conclusão da operação.
     */
    public Mono<Void> deletarSalaComInscricoes(Long id) {
        return inscricaoRepository.deleteByIdSala(id)
            .then(salaRepository.deleteById(id));
    }    
}
