package pl.sda;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GreetingService {

    private GreetingsRepository repository;
    private GreetingMapper mapper;

    public GreetingService(GreetingsRepository repository , GreetingMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public Greeting createGreeting(CreateGreetingDTO command){
       return repository.save(mapper.toModel(command));
    }

    public Iterable<Greeting> findAll() { return repository.findAll();}

    @Transactional
    public void createMultipleGreetings(CreateMultipleGreetingDTO command) {
            command.getCommands().forEach(this::createGreeting);

    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Greeting> findById(Long id) {
        return repository.findById(id);
    }
}
