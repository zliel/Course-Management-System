package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.User;
import com.personal.springbootpractice.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserRestController {

    final UserRepository repository;

    public UserRestController(UserRepository repository) { this.repository = repository; }


    @GetMapping("/api/users")
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/api/users/{id}")
    public Mono<User> getUserById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    @GetMapping("/api/users/{schoolName}")
    public Flux<User> getUsersBySchoolName(@PathVariable("schoolName") String schoolName) {
        return repository.findAllBySchoolName(schoolName);
    }
}
