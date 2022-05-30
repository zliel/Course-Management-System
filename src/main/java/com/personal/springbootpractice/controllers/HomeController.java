package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {


    /**
     * @param model Automatically passed in by Spring Boot
     * @return A Mono with string which serves the Home.html page
     */
    @GetMapping("/")
    public Mono<String> home(Model model) {
        model.addAttribute("isAuthenticated", AuthenticationUtils.isAuthenticated());
        return Mono.just("Home");
    }
}
