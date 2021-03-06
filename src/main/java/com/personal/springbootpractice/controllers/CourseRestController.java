package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.Course;
import com.personal.springbootpractice.repositories.CourseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@RestController
@Api(tags = "Courses")
public class CourseRestController {

    final CourseRepository repository;

    public CourseRestController(CourseRepository repository) {
        this.repository = repository;
    }

    /**
     * @return A Flux containing all courses in the database
     */
    @GetMapping("/api/courses")
    @ApiOperation(value = "Retrieves all courses")
    public Flux<Course> getAllCourses() {
        return repository.findAll();
    }

    /**
     * @param id The id of the course to be retrieved
     * @return A Mono containing the course if it exists
     */
    @GetMapping("/api/courses/id={id}")
    @ApiOperation(value = "Retrieves a course by its id")
    public Mono<Course> getCourseById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    /**
     * @param schoolName The name of a school, which retrieves all courses associated with the provided school
     * @return A Flux containing all the courses associated with the provided school
     */
    @GetMapping("/api/courses/school={schoolName}")
    @ApiOperation(value = "Retrieves all courses from a given school")
    public Flux<Course> getCoursesBySchoolName(@PathVariable("schoolName") String schoolName) {
        return repository.findAllBySchoolName(schoolName);
    }

    /** This method will be changed with the next update
     * @deprecated
     * @param name
     * @return
     */
    @PostMapping("/api/courses/new/{name}")
    @ApiOperation(value = "Adds a new course with the given name (currently with default values)")
    public Mono<String> addNewCourse(@PathVariable("name") String name) {
        Course courseToAdd = new Course(name, new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30, "UCSD");

        return repository.save(courseToAdd).then(Mono.just("Successfully Added!"));
    }

    /**
     * @param id The id of the course to be edited
     * @param newName The new name to give the edited course
     * @param maxStudents The new number of max students to give the edited course
     * @param fromDate The new starting date for the edited course
     * @param toDate The new ending date for hte edited course
     * @return A Mono containing the updated course
     */
    @PostMapping("/api/courses/edit")
    @ApiOperation(value = "Edits a course with the provided parameters")
    public Mono<Course> updateCourse(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "name", required = false) Optional<String> newName,
            @RequestParam(value = "maxStudents", required = false) Optional<Integer> maxStudents,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> fromDate,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> toDate
    )
    {
        return repository.findById(id)
                .map(course -> {
                    newName.ifPresent(course::setName);
                    maxStudents.ifPresent(course::setMaxStudents);
                    fromDate.ifPresent(course::setStartDate);
                    toDate.ifPresent(course::setEndDate);
                    return course;
                })
                .flatMap(repository::save)
                .log("Updating Course");
    }

    /**
     * @param id The id of the course to be deleted
     * @return A Mono containing a string with either a success or error message
     */
    @DeleteMapping("/api/courses/delete/{id}")
    @ApiOperation(value = "Deletes a course by its id")
    public Mono<String> deleteCourse(@PathVariable("id") String id) {
        Mono<Course> courseToDelete = repository.findById(id);

        return courseToDelete.flatMap(repository::delete)
                            .then(Mono.just("Successfully Deleted!"))
                            .onErrorReturn("Error: Couldn't delete course");
    }
}
