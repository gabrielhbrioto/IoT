package com.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.ZonedDateTime;

@Table("MEDIDA")
public class Medida {

    @Id
    private Long id;

    private Sensor sensor;

    private Integer valor;

    private ZonedDateTime horario;

    // Getter e Setter para o ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter para o Sensor
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    // Getter e Setter para o Valor
    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    // Getter e Setter para o Horário
    public ZonedDateTime getHorario() {
        return horario;
    }

    public void setHorario(ZonedDateTime horario) {
        this.horario = horario;
    }
}
