package com.iot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.ZonedDateTime;

/**
 * Classe que representa uma medida.
 * Mapeada para a tabela "MEDIDA".
 */
@Table("MEDIDA")
public class Medida {

    @Id
    private Long id;

    private Long idSala;

    private double valor;

    private ZonedDateTime horario;

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public ZonedDateTime getHorario() {
        return horario;
    }

    public void setHorario(ZonedDateTime horario) {
        this.horario = horario;
    }
}
