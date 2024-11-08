package com.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.ZonedDateTime;

@Table("MEDIDA")
public class Medida {

    @Id
    private Long id;

    private Long idSala; // Alterado para referenciar o ID da sala

    private double valor;

    private ZonedDateTime horario;

    // Getter e Setter para o ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter para o ID da Sala
    public Long getIdSala() {
        return idSala;
    }

    public void setIdSala(Long idSala) {
        this.idSala = idSala;
    }

    // Getter e Setter para o Valor
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
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
