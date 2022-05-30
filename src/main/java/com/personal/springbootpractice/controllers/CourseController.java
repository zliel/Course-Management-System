package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.models.User;
import com.personal.springbootpractice.repositories.CourseRepository;
import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

/**
 * This Controller is responsible for all non-REST requests involving Courses, including creating, editing, retrieving and deleting courses
 *
 * @author Zac
 * @since 08/01/21
 */
@Controller
public class CourseController {

    final CourseRepository repository;

    public CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    /**
     * This method retrieves all courses from the database and adds them to the returned view
     *
     * @param model - Passed in automatically by Spring Boot
     * @return A Mono containing a string which serves the Courses.html page
     */
    @GetMapping("/allcourses")
    public Mono<String> allCourses(Model model, @AuthenticationPrincipal User user) {

        model.addAttribute("isAuthenticated", AuthenticationUtils.isAuthenticated());
        model.addAttribute("courses", repository.findAllBySchoolName(user.getSchoolName()).sort());
        model.addAttribute("course", new Course());
        return Mono.just("Courses");
    }

    /**
     * This method saves a new course from the values input into the form on the "/allcourses" page
     * @param course Passed in automatically by Thymeleaf from the form on the Courses.html page
     * @return A Mono containing a string which redirects the user to the Courses.html page
     */
    @PostMapping("/courses/new")
    public Mono<String> newCourse(@ModelAttribute(value="course") Course course, @AuthenticationPrincipal User user) {
        course.setSchoolName(user.getSchoolName());
        // Solution to the issue of getting form data from POST form: https://stackoverflow.com/questions/17669212/send-datas-from-html-to-controller-in-thymeleaf
        // Solution to the date type mismatch error: https://stackoverflow.com/questions/53188464/spring-boot-date-conversion-from-form


        return repository.save(course).then(Mono.just("redirect:/allcourses"));
    }

    /**
     * This method takes the user to a page to edit a course found by its ID
     *
     * @param id The id of the course to be edited
     * @param model Passed in automatically by Spring Boot
     * @return A Mono containing a string which serves the Edit-Course.html page
     */
    @GetMapping("/courses/edit/{id}")
    public Mono<String> editCourse(@PathVariable("id") String id, Model model) {
        Mono<Course> courseToEdit = repository.findById(id);
        model.addAttribute("course", courseToEdit);
        model.addAttribute("isAuthenticated", AuthenticationUtils.isAuthenticated());

        return Mono.just("edit-course");
    }

    /**
     * This method handles deleting courses using their IDs (Will likely be changed to a DELETE mapping)
     * @param id The id of the course to be deleted
     * @return A Mono containing a string which redirects the user to the Courses.html page
     */
    @GetMapping("/courses/delete/{id}")
    public Mono<String> deleteCourse(@PathVariable("id") String id) {
        Mono<Course> courseToDelete = repository.findById(id);

        return courseToDelete.flatMap(repository::delete).then(Mono.just("redirect:/allcourses"));
    }
}
