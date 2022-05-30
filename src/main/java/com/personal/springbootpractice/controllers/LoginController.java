package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {

    /**
     * @return A Mono with a string redirecting to either the Login.html page for an anonymous user, or the Home.html page for users who are already logged in
     */
    @GetMapping("/login")
    public Mono<String> login() {
        return AuthenticationUtils.authenticateEndpoint("Login");
        }

 }
