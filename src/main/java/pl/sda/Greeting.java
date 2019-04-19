package pl.sda;


import lombok.Data;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Greeting {

    private String msg;
    @Id
    @GeneratedValue
    private long id;

    private String author;


}
