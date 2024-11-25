package com.iot.service;

import com.iot.model.Inscricao;
import com.iot.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Serviço para gerenciar inscrições.
 */
@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    /**
     * Retorna todas as inscrições.
     *
     * @return Flux<Inscricao> - Fluxo contendo todas as inscrições.
     */
    public Flux<Inscricao> getAllInscricoes() {
        return inscricaoRepository.findAll();
    }

    /**
     * Cria uma nova inscrição.
     *
     * @param inscricao - Objeto Inscricao a ser criado.
     * @return Mono<Inscricao> - Mono contendo a inscrição criada.
     */
    public Mono<Inscricao> createInscricao(Inscricao inscricao) {
        return inscricaoRepository.save(inscricao);
    }

    /**
     * Deleta uma inscrição pelo ID.
     *
     * @param id - ID da inscrição a ser deletada.
     * @return Mono<Void> - Mono indicando a conclusão da operação.
     */
    public Mono<Void> deleteInscricao(Long id) {
        return inscricaoRepository.deleteById(id);
    }

    /**
     * Retorna inscrições pelo ID do usuário.
     *
     * @param idUsuario - ID do usuário.
     * @return Flux<Inscricao> - Fluxo contendo as inscrições do usuário.
     */
    public Flux<Inscricao> getInscricoesByUsuarioId(Long idUsuario) {
        return inscricaoRepository.findByIdUsuario(idUsuario);
    }

    /**
     * Deleta uma inscrição pelo ID do usuário e ID da sala.
     *
     * @param userId - ID do usuário.
     * @param salaId - ID da sala.
     * @return Mono<Void> - Mono indicando a conclusão da operação.
     */
    public Mono<Void> deleteByUserIdAndSalaId(Long userId, Long salaId) {
        return inscricaoRepository.deleteByIdUsuarioAndIdSala(userId, salaId);
    }
}
