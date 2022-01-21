package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
public class CourseRestController {

    @Autowired
    CourseRepository repository;

    @GetMapping("/api/courses/{id}")
    public Mono<Course> getCourse(@PathVariable("id") Long id) {
        Mono<Course> foundCourse = repository.findById(id);
        return foundCourse;
    }

    // This would be changed to a POST mapping
    @GetMapping("/api/courses/new/{id}")
    public Mono<String> addNewCourse(@PathVariable("id") Long id) {
        Course courseToAdd = new Course("Newly Made Course!", new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30);

        return repository.save(courseToAdd).then(Mono.just("Success!"));
    }

    // This should be changed to a DELETE mapping, and the HttpHiddenFilter Bean might need to be added
    @GetMapping("/api/courses/delete/{id}")
    public Mono<String> deleteCourse(@PathVariable Long id) {
        Mono<Course> courseToDelete = repository.findById(id);

        return courseToDelete.flatMap(repository::delete)
                            .then(Mono.just("Successfully Deleted!"));
    }
}
