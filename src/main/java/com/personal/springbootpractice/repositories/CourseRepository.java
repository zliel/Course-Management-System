package com.personal.springbootpractice.repositories;


import com.personal.springbootpractice.models.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
    Mono<Course> findById(String id);
}
