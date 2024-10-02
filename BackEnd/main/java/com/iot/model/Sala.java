@Table("SALA")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ID_CRIADOR", nullable = false)
    private Usuario criador;

    // getters e setters
}
