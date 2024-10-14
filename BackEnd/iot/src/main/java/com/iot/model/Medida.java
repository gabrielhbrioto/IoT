package com.iot.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import java.time.ZonedDateTime;

@Entity
@Table(name = "MEDIDA")
public class Medida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_SENSOR", nullable = false)
    private Sensor sensor;

    private Integer valor;

    @Column(name = "HORARIO", nullable = false)
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
