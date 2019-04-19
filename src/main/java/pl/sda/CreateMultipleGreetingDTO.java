package pl.sda;

import lombok.Data;

@Data
public class CreateMultipleGreetingDTO {
    private Iterable<CreateGreetingDTO> commands;
}
