package com.personal.springbootpractice.repositories;


import com.personal.springbootpractice.models.School;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface SchoolRepository extends ReactiveCrudRepository<School, String> {
    Mono<School> findById(String id);
}
