package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    @GetMapping("/")
    public Mono<String> home(Model model) {
        model.addAttribute("isAuthenticated", AuthenticationUtils.isAuthenticated());
        return Mono.just("Home");
    }
}
