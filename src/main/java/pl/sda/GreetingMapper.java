package pl.sda;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// różnica semantyczna
// @Component i  @Service
@Service
public class GreetingMapper {

       public  Greeting toModel(CreateGreetingDTO dto) {
           Greeting greeting = new Greeting();
           greeting.setMsg(dto.getMsg());
           return greeting;
       };
}
