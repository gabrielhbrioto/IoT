package com.iot.model;

import org.springframework.data.annotation.Id;

public class Sensor {

    @Id
    private Long id;
    private Long idSala; 
    private String tipo;

    // Construtor padrão
    public Sensor() {}

    // Construtor com parâmetros
    public Sensor(Long id, Long idSala, String tipo) {
        this.id = id;
        this.idSala = idSala;
        setTipo(tipo); // Usa o setter para validar
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSala() {
        return idSala;
    }
    
    public void setIdSala(Long idSala) {
        this.idSala = idSala;
    }    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (!tipo.equals("CORRENTE") && !tipo.equals("TENSAO") && !tipo.equals("PRESENCA")) {
            throw new IllegalArgumentException("Tipo deve ser 'CORRENTE', 'TENSAO' ou 'PRESENCA'");
        }
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", idSala=" + idSala +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
