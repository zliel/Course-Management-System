package com.personal.springbootpractice.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {

    @GetMapping("/login")
    public Mono<String> login() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::isAuthenticated)
                .flatMap(loggedIn -> {
                    if(loggedIn) { return Mono.just("redirect:/"); }
                    return Mono.just("Login");
                });
        }

 }
