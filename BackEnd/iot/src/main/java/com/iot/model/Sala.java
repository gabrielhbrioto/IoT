package com.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Classe que representa uma sala.
 * Mapeada para a tabela "SALA".
 */
@Table("SALA")
public class Sala {
    
    @Id
    private Long id;

    private String nome;

    private Long idCriador;

    private String estado; 
    
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
