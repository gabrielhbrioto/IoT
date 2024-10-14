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
        return inscricaoRepository.findAll(); // Retorna um Flux de inscrições
    }

    public Mono<Inscricao> createInscricao(Inscricao inscricao) {
        return inscricaoRepository.save(inscricao); // Método para salvar uma nova inscrição
    }

    public Mono<Void> deleteInscricao(Long id) {
        return inscricaoRepository.deleteById(id); // Método para deletar a inscrição pelo ID
    }
}
