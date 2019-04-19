package pl.sda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/greetings")
public class HelloWorldController {

    private GreetingService greetingService;

    public HelloWorldController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping
    public Iterable<Greeting> getAllGreetings(){
        //metody z repozytorium automatycznie są transakcyjne (albo wszystko wykona albo nic się nie wykona  )
                return greetingService.findAll();
    }

    // przy postach,patchach,putach uzywac request body czyli ciało ktore zostanie wyslane
    @PostMapping
    public Greeting addGreeting(@RequestBody CreateGreetingDTO command){
       return greetingService.createGreeting(command);
    }

    @DeleteMapping("/{id}")
    public void removeGreeting (@PathVariable("id") Long id){
        greetingService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Optional<Greeting> getGreetingById (@PathVariable Long id){
        return greetingService.findById(id);
    }
}
