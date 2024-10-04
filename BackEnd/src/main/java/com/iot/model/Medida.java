@Table("MEDIDA")
public class Medida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_SENSOR")
    private Sensor sensor;

    private Integer valor;

    @Column(name = "HORARIO")
    private ZonedDateTime horario;

    // getters e setters
}
