package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.User;
import com.personal.springbootpractice.repositories.SchoolRepository;
import com.personal.springbootpractice.repositories.UserRepository;
import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class UserController {

    final UserRepository userRepository;
    final SchoolRepository schoolRepository;

    final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, SchoolRepository schoolRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param model Passed in automatically by Spring Boot
     * @return A Mono containing a string which serves the Signup.html page
     */
    @GetMapping("/signup")
    public Mono<String> signup(Model model) {
        model.addAttribute("isAuthenticated", AuthenticationUtils.isAuthenticated());
        model.addAttribute("user", new User());
        model.addAttribute("schools", schoolRepository.findAll().sort());
        return AuthenticationUtils.authenticateEndpoint("Signup");
    }

    /**
     * @param newUser Passed in automatically by Thymeleaf from the form on the Signup.html page
     * @return A Mono containing a string which redirects the user to the Login.html page
     */
    @PostMapping("/users/new")
    public Mono<String> createNewUser(@ModelAttribute(value="user") User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.existsUserByUsername(newUser.getUsername())
               .flatMap(userExists -> {
                    if(userExists) return Mono.just("redirect:/");
                    return userRepository.save(newUser).then(Mono.just("redirect:/login"));
        });
    }

    /**
     * @param model Passed in automatically by Spring Boot
     * @return A Mono containing a string which serves the User-Delete.html page
     */
    @GetMapping("/users/delete")
    public Mono<String> userDeletionPage(Model model) {
        model.addAttribute("user", new User());
        return Mono.just("user-delete");
    }

    /**
     * @param user Passed in automatically by Thymeleaf from the form on the User-Delete.html page
     * @return A Mono containing a string which redirects the user to the home page
     */
    @PostMapping("/users/delete/")
    public Mono<String> deleteUser(@ModelAttribute(value="user") User user) {
        String username = user.getUsername();
        return userRepository.findUserByUsername(username)
                .flatMap(userRepository::delete)
                .then(Mono.just("redirect:/"));
    }
}
