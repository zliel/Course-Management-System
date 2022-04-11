package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * This Controller is responsible for all non-REST requests involving Courses, including creating, editing, retrieving and deleting courses
 *
 * @author Zac
 * @since 08/01/21
 */
@Controller
public class CourseController {

    @Autowired
    CourseRepository repository;

    /**
     * This method retrieves all courses from the database and adds them to the returned view
     *
     * @param model - The view to which the courses are added
     * @return A string containing the name of the corresponding HTML file to which the user will be sent to
     */
    @GetMapping("/allcourses")
    public Mono<String> allCourses(Model model) {
        model.addAttribute("courses", repository.findAll().sort());
        model.addAttribute("course", new Course());
        return Mono.just("Courses");
    }

    /**
     * This method saves a new course from the values input into the form on the "/allcourses" page
     * @param course This Course object is created using Thymeleaf from the values input into the "/allcourses" new Course form
     * @return A redirect to the "/allcourses" page
     */
    @PostMapping("/courses/new")
    public Mono<String> newCourse(@ModelAttribute(value="course") Course course) {
        System.out.println(course);
        // Solution to the issue of getting form data from POST form: https://stackoverflow.com/questions/17669212/send-datas-from-html-to-controller-in-thymeleaf
        // Solution to the date type mismatch error: https://stackoverflow.com/questions/53188464/spring-boot-date-conversion-from-form


        return repository.save(course).then(Mono.just("redirect:/allcourses"));
    }

    /**
     * This method takes the user to a page to edit a course found by its ID
     *
     * @param id The ID of the course to be edited
     * @param model The model to which the found course's attributes are added
     * @return A string containing the name of the corresponding HTML file to which the user will be sent to
     */
    @GetMapping("/courses/edit/{id}")
    public Mono<String> editCourse(@PathVariable("id") String id, Model model) {
        Mono<Course> courseToEdit = repository.findById(id);
        model.addAttribute("course", courseToEdit);

        return Mono.just("edit-course");
    }

    /**
     * This method handles deleting courses using their IDs (Will likely be changed to a DELETE mapping)
     * @param id The ID of the course to be deleted
     * @return A redirect to the "/allcourses" page
     */
    @GetMapping("/courses/delete/{id}")
    public Mono<String> deleteCourse(@PathVariable("id") String id) {
        Mono<Course> courseToDelete = repository.findById(id);

        return courseToDelete.flatMap(repository::delete).then(Mono.just("redirect:/allcourses"));
    }
}
