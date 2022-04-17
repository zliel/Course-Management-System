package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {

    @GetMapping("/login")
    public Mono<String> login() {
        return AuthenticationUtils.authenticateEndpoint("Login");
        }

 }
