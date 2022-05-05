package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.repositories.CourseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
public class CourseRestController {

    final CourseRepository repository;

    public CourseRestController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/courses")
    public Flux<Course> getAllCourses() {
        return repository.findAll();
    }

    @GetMapping("/api/courses/{id}")
    public Mono<Course> getCourse(@PathVariable("id") Long id) {
        return repository.findById(id);
    }

    @GetMapping("/api/courses/{schoolName}")
    public Flux<Course> getCoursesBySchoolName(@PathVariable("schoolName") String schoolName) {
        return repository.findAllBySchoolName(schoolName);
    }

    // This would be changed to a POST mapping
    @PostMapping("/api/courses/new/{id}")
    public Mono<String> addNewCourse(@PathVariable("id") Long id) {
        Course courseToAdd = new Course("Newly Made Course!", new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30, "UCSD");

        return repository.save(courseToAdd).then(Mono.just("Success!"));
    }

    // This should be changed to a DELETE mapping, and the HttpHiddenFilter Bean might need to be added
    @PostMapping("/api/courses/delete/{id}")
    public Mono<String> deleteCourse(@PathVariable Long id) {
        Mono<Course> courseToDelete = repository.findById(id);

        return courseToDelete.flatMap(repository::delete)
                            .then(Mono.just("Successfully Deleted!"));
    }
}
