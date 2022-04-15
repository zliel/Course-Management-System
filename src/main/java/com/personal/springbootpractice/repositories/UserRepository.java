package com.personal.springbootpractice.repositories;

import com.personal.springbootpractice.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<UserDetails> findByUsername(String username);
    Mono<Boolean> existsUserByUsername(String username);

}
