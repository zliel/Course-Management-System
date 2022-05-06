package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.models.School;
import com.personal.springbootpractice.repositories.CourseRepository;
import com.personal.springbootpractice.repositories.SchoolRepository;
import com.personal.springbootpractice.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@Api(tags = "Schools")
public class SchoolRestController {

    final SchoolRepository schoolRepository;
    final CourseRepository courseRepository;
    final UserRepository userRepository;

    public SchoolRestController(SchoolRepository schoolRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/schools")
    @ApiOperation(value = "Retrieves all schools")
    public Flux<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    @GetMapping("/api/schools/id={id}")
    @ApiOperation(value = "Retrieves a school by its id")
    public Mono<School> getSchoolById(@PathVariable("id") String id) {
        return schoolRepository.findById(id);
    }

    @GetMapping("/api/schools/name={name}")
    @ApiOperation(value = "Retrieves a school by its name")
    public Mono<School> getSchoolByName(@PathVariable("name") String name) {
        return schoolRepository.findByName(name);
    }

    @PostMapping("/api/schools/new/{name}")
    @ApiOperation(value = "Creates a new school with the provided name")
    public Mono<String> addSchool(@PathVariable("name") String name) {
        return schoolRepository.save(new School(name)).then(Mono.just("Successfully saved!"));
    }

    @PostMapping("/api/schools/edit")
    @ApiOperation(value = "Edits a school with the provided parameters")
    public Mono<School> updateSchool(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "name", required = false) Optional<String> newName
    )
    {
        return schoolRepository.findById(id)
                .map(school -> {
                    newName.ifPresent(school::setName);
                    return school;
                })
                .map(school -> {
                    newName.ifPresent(name -> {
                        courseRepository.findAllBySchoolName(school.getName())
                                .log("Updating Associated Courses")
                                .map(course -> {
                                    course.setSchoolName(name);
                                    return course;
                                });
                        userRepository.findAllBySchoolName(school.getName())
                                .log("Updating Associated Users")
                                .map(user -> {
                                    user.setSchoolName(name);
                                    return user;
                                });
                    });
                    return school;
                })
                .flatMap(schoolRepository::save)
                .log("Updating School");
    }

    @DeleteMapping("/api/schools/delete/{id}")
    @ApiOperation(value = "Removes a school by name, and removes its associated users and courses")
    public Mono<String> deleteSchool(@PathVariable("id") String id) {
        return schoolRepository.findById(id)
                .log("School Search")
                .doOnEach(System.out::println)
                .flatMap(s -> {
                    return courseRepository.findAllBySchoolName(s.getName())
                            .log("Course Deletion")
                            .doOnEach(System.out::println)
                            .flatMap(courseRepository::delete)
                            .then(Mono.just(s));
                })
                .flatMap(s -> {
                    return userRepository.findAllBySchoolName(s.getName())
                            .log("User Deletion")
                            .doOnEach(System.out::println)
                            .flatMap(userRepository::delete)
                            .then(Mono.just(s));
                })
                .log("School Deletion")
                .doOnEach(System.out::println)
                .flatMap(schoolRepository::delete)
                .then(Mono.just("Successfully deleted!"));
    }
}
