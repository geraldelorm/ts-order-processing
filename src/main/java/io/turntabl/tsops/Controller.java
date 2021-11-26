package io.turntabl.tsops;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping
    public String home(){
        return "Hello, Welcome to Order Processing Service";
    }
}
