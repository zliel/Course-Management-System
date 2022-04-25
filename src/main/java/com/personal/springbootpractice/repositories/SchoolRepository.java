package com.personal.springbootpractice.repositories;


import com.personal.springbootpractice.models.School;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface SchoolRepository extends ReactiveCrudRepository<School, Long> {
}
