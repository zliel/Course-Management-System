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


    /**
     * @return A Flux containing all users in the database
     */
    @GetMapping("/api/users")
    @ApiOperation(value = "Retrieves all users")
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    /**
     * @param id The id of the user to be retrieved
     * @return A Mono containing the user if it exists
     */
    @GetMapping("/api/users/id={id}")
    @ApiOperation(value = "Retrieves user by Id", response = User.class)
    public Mono<User> getUserById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    /**
     * @param schoolName The name of a school, which retrieves all users associated with the provided school
     * @return A Flux containing all users associated with the provided school
     */
    @GetMapping("/api/users/school={schoolName}")
    @ApiOperation(value = "Retrieves users by the entered school name")
    public Flux<User> getUsersBySchoolName(@PathVariable("schoolName") String schoolName) {
        return repository.findAllBySchoolName(schoolName);
    }

    /**
     * @param username The username to give the new user
     * @param password The password to give the new user (hashed prior to storage)
     * @param schoolName The name of the school the user is associated with
     * @param roles A list of roles to give the new user
     * @return A Mono containing a string with either a success or error message
     */
    @PostMapping("/api/users/new")
    @ApiOperation(value = "Creates a new user with the given parameters")
    public Mono<String> newUser(
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
                .then(Mono.just("Successfully Created!"))
                .onErrorReturn("Error: Could not create new user");
    }

    /**
     * @param id The id of the user to be edited
     * @param newUsername The new username to give the edited user
     * @param newPassword The new password to give the edited user
     * @param newSchoolName The new school to associate the edited user with
     * @param newRoles A list of new roles to add to the edited user
     * @return A Mono containing a string with either a success or error message
     */
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
                .then(Mono.just("Successfully Updated!"))
                .onErrorReturn("Error: Could not update user");
    }

    /**
     * @param id The id of the user to be deleted
     * @return A Mono containing a string with either a success or error message
     */
    @DeleteMapping("/api/users/delete/{id}")
    @ApiOperation(value = "Deletes a user with the provided id")
    public Mono<String> deleteUser(@PathVariable("id") String id) {
        return repository.deleteById(id)
                .then(Mono.just("Successfully Deleted!"))
                .onErrorReturn("Error: Could not delete user");
    }
}
