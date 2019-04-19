package pl.sda;

import org.springframework.data.repository.CrudRepository;

public interface GreetingsRepository extends CrudRepository<Greeting, Long> {
}
