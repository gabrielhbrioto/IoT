import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SensorRepository extends ReactiveCrudRepository<Sensor, Long> {
}
