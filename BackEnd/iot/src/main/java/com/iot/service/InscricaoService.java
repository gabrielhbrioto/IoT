package com.iot.service;

import com.iot.model.Inscricao;
import com.iot.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    public Flux<Inscricao> getAllInscricoes() {
        return inscricaoRepository.findAll();
    }

    public Mono<Inscricao> createInscricao(Inscricao inscricao) {
        return inscricaoRepository.save(inscricao);
    }

    public Mono<Void> deleteInscricao(Long id) {
        return inscricaoRepository.deleteById(id);
    }

    public Flux<Inscricao> getInscricoesByUsuarioId(Long idUsuario) {
        return inscricaoRepository.findByIdUsuario(idUsuario);
    }

    // Novo método para deletar uma inscrição pelo ID do usuário e o ID da sala
    public Mono<Void> deleteByUserIdAndSalaId(Long userId, Long salaId) {
        return inscricaoRepository.deleteByIdUsuarioAndIdSala(userId, salaId);
    }
}
