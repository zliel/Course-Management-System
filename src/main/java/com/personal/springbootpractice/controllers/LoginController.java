package com.personal.springbootpractice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {



    @PostMapping("/login")
    public Mono<String> submitLogin() {
        return Mono.just("/");
    }
 }
