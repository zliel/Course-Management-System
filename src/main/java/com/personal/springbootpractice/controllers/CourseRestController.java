package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.repositories.CourseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
@Api(tags = "Courses")
public class CourseRestController {

    final CourseRepository repository;

    public CourseRestController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/courses")
    @ApiOperation(value = "Retrieves all courses")
    public Flux<Course> getAllCourses() {
        return repository.findAll();
    }

    @GetMapping("/api/courses/id={id}")
    @ApiOperation(value = "Retrieves a course by its id")
    public Mono<Course> getCourseById(@PathVariable("id") Long id) {
        return repository.findById(id);
    }

    @GetMapping("/api/courses/school={schoolName}")
    @ApiOperation(value = "Retrieves all courses from a given school")
    public Flux<Course> getCoursesBySchoolName(@PathVariable("schoolName") String schoolName) {
        return repository.findAllBySchoolName(schoolName);
    }

    @PostMapping("/api/courses/new/{name}")
    @ApiOperation(value = "Adds a new course with the given name (currently with default values)")
    public Mono<String> addNewCourse(@PathVariable("name") String name) {
        Course courseToAdd = new Course(name, new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30, "UCSD");

        return repository.save(courseToAdd).then(Mono.just("Successfully Added!"));
    }

    @DeleteMapping("/api/courses/delete/{id}")
    @ApiOperation(value = "Deletes a course by its id")
    public Mono<String> deleteCourse(@PathVariable("id") String id) {
        Mono<Course> courseToDelete = repository.findById(id);

        return courseToDelete.flatMap(repository::delete)
                            .then(Mono.just("Successfully Deleted!"));
    }
}
