import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {
    Mono<Usuario> findByEmail(String email);
}
