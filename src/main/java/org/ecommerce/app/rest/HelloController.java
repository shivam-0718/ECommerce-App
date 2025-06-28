package org.ecommerce.app.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/home")
    public String greet() {
        return "Welcome to ECommerce application.";
    }
}
