package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.School;
import com.personal.springbootpractice.repositories.SchoolRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Api(tags = "School REST Controller")
public class SchoolRestController {

    final SchoolRepository repository;

    public SchoolRestController(SchoolRepository repository) { this.repository = repository; }

    @GetMapping("/api/schools")
    @ApiOperation(value = "Retrieves all schools")
    public Flux<School> getAllSchools() {
        return repository.findAll();
    }

    @GetMapping("/api/schools/{id}")
    @ApiOperation(value = "Retrieves a school by its id")
    public Mono<School> getSchoolById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    @GetMapping("/api/schools/{name}")
    @ApiOperation(value = "Retrieves a school by its name")
    public Mono<School> getSchoolByName(@PathVariable("name") String name) {
        return repository.findByName(name);
    }

    @PostMapping("/api/schools/new/{name}")
    @ApiOperation(value = "Creates a new school with the provided name")
    public Mono<String> addSchool(@PathVariable("name") String name) {
        return repository.save(new School(name)).then(Mono.just("Successfully saved!"));
    }
}
