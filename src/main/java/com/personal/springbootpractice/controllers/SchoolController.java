package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.School;
import com.personal.springbootpractice.repositories.SchoolRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class SchoolController {

    final SchoolRepository repository;

    public SchoolController(SchoolRepository repository) {
        this.repository = repository;
    }

    @GetMapping("schools/new")
    public Mono<String> newSchool(Model model) {
        model.addAttribute("school", new School());
        return Mono.just("New-School");
    }

    @PostMapping("schools/new")
    public Mono<String> createNewSchool(@ModelAttribute(value="school") School newSchool) {
        return repository.save(newSchool).then(Mono.just("redirect:/signup"));
    }
}
