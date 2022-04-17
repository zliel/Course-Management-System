package com.personal.springbootpractice.controllers;

import com.personal.springbootpractice.models.User;
import com.personal.springbootpractice.repositories.UserRepository;
import com.personal.springbootpractice.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public Mono<String> signup(Model model) {
        model.addAttribute("isAuthenticated", AuthenticationUtils.isAuthenticated());
        model.addAttribute("user", new User());
        return AuthenticationUtils.authenticateEndpoint("Signup");
    }


    @PostMapping("/users/new")
    public Mono<String> createNewUser(@ModelAttribute(value="user") User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.existsUserByUsername(newUser.getUsername())
               .flatMap(userExists -> {
                    if(userExists) return Mono.just("redirect:/");
                    return userRepository.save(newUser).then(Mono.just("redirect:/login"));
        });
    }

    @GetMapping("/users/delete")
    public Mono<String> userDeletionPage(Model model) {
        model.addAttribute("user", new User());
        return Mono.just("user-delete");
    }

    @PostMapping("/users/delete/")
    public Mono<String> deleteUser(@ModelAttribute(value="user") User user) {
        String username = user.getUsername();
        return userRepository.findUserByUsername(username)
                .flatMap(userRepository::delete)
                .then(Mono.just("redirect:/"));
    }
}
