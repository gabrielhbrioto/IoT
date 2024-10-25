package com.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("SALA")
public class Sala {
    
    @Id
    private Long id;

    private String nome;

    private Long idCriador; // Mapeando apenas o ID do criador

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(Long idCriador) {
        this.idCriador = idCriador;
    }
}
