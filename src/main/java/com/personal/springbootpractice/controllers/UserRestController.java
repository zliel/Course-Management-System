package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.User;
import com.personal.springbootpractice.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Users")
public class UserRestController {

    final UserRepository repository;
    final PasswordEncoder passwordEncoder;

    public UserRestController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/api/users")
    @ApiOperation(value = "Retrieves all users")
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/api/users/id={id}")
    @ApiOperation(value = "Retrieves user by Id", response = User.class)
    public Mono<User> getUserById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    @GetMapping("/api/users/school={schoolName}")
    @ApiOperation(value = "Retrieves users by the entered school name")
    public Flux<User> getUsersBySchoolName(@PathVariable("schoolName") String schoolName) {
        return repository.findAllBySchoolName(schoolName);
    }

    @PostMapping("/api/users/new")
    @ApiOperation(value = "Creates a new user with the given parameters")
    public Mono<String> editUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "school") String schoolName,
            @RequestParam(value = "roles", required = false) Optional<List<String>> roles
    )
    {
        User newUser = new User(username, passwordEncoder.encode(password), schoolName);
        roles.ifPresent(roleList -> roleList.forEach(role -> newUser.getRoles().add(new SimpleGrantedAuthority(role))));
        return repository.save(newUser)
                .log("User Creation")
                .then(Mono.just("Successfully Created!"));
    }

    @PostMapping("/api/users/edit")
    @ApiOperation(value = "Edits a user with the given parameters")
    public Mono<String> editUser(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "username", required = false) Optional<String> newUsername,
            @RequestParam(value = "password", required = false) Optional<String> newPassword,
            @RequestParam(value = "school", required = false) Optional<String> newSchoolName,
            @RequestParam(value = "roles", required = false) Optional<List<String>> newRoles
    )
    {
        return repository.findById(id)
                .log("Updating User")
                .map(user -> {
                    newUsername.ifPresent(user::setUsername);
                    newPassword.ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
                    newSchoolName.ifPresent(user::setSchoolName);
                    newRoles.ifPresent(roleList -> roleList.forEach(role -> user.getRoles().add(new SimpleGrantedAuthority(role))));
                    return user;
                })
                .flatMap(repository::save)
                .then(Mono.just("Successfully Updated!"));
    }

    @DeleteMapping("/api/users/delete/{id}")
    @ApiOperation(value = "Deletes a user with the provided id")
    public Mono<String> newUser(@PathVariable("id") String id) {
        return repository.deleteById(id).then(Mono.just("Successfully Deleted!"));
    }
}
