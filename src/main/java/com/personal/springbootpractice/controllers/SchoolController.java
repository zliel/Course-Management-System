package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.School;
import com.personal.springbootpractice.repositories.CourseRepository;
import com.personal.springbootpractice.repositories.SchoolRepository;
import com.personal.springbootpractice.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class SchoolController {

    final SchoolRepository schoolRepository;
    final CourseRepository courseRepository;
    final UserRepository userRepository;

    public SchoolController(SchoolRepository schoolRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("schools/new")
    public Mono<String> newSchool(Model model) {
        model.addAttribute("school", new School());
        return Mono.just("New-School");
    }

    @PostMapping("schools/new")
    public Mono<String> createNewSchool(@ModelAttribute(value="school") School newSchool) {
        return schoolRepository.save(newSchool).then(Mono.just("redirect:/signup"));
    }

    @GetMapping("schools/delete")
    public Mono<String> deleteSchool(Model model) {
        model.addAttribute("schoolModel", new School());
        model.addAttribute("schools", schoolRepository.findAll().sort());
        return Mono.just("Delete-School");
    }

    @PostMapping("schools/delete")
    public Mono<String> deleteSchool(@ModelAttribute(value="schoolModel") School school) {
        System.out.println(school);
        return schoolRepository.findById(school.getId())
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
                .log("Deleting School")
                .doOnEach(System.out::println)
                .flatMap(schoolRepository::delete)
                .then(Mono.just("redirect:/schools/delete"));

    }
}
