package com.iot.model;

public class Sensor {

    private Long id;
    private Long salaId; // Certifique-se de ter este campo
    private String tipo;

    // Construtor padrão
    public Sensor() {}

    // Construtor com parâmetros
    public Sensor(Long id, Long salaId, String tipo) {
        this.id = id;
        this.salaId = salaId;
        setTipo(tipo); // Usa o setter para validar
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalaId() {
        return salaId;
    }
    
    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (!tipo.equals("CORRENTE") && !tipo.equals("TENSAO")) {
            throw new IllegalArgumentException("Tipo deve ser 'CORRENTE' ou 'TENSAO'");
        }
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", salaId=" + salaId +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
