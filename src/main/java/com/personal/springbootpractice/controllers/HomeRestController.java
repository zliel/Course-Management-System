package com.personal.springbootpractice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HomeRestController {

    @GetMapping("/api")
    public Mono<String> hello() {
        return Mono.just("Hello");
    }
}
