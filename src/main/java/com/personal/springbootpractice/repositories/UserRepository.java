package com.personal.springbootpractice.repositories;

import com.personal.springbootpractice.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<UserDetails> findUserDetailsByUsername(String username);
    Mono<User> findUserByUsername(String username);
    Mono<Boolean> existsUserByUsername(String username);
    Flux<User> findAllBySchoolName(String schoolName);
    Mono<User> findById(String id);
    Mono<Void> deleteById(String id);
}
