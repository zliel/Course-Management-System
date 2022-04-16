package com.personal.springbootpractice;

import com.personal.springbootpractice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Autowired
    UserRepository userRepository;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers("/courses/delete/**", "/courses/edit/**", "/users/delete/**", "users/delete")
                .hasRole("ADMIN")
                .and()
            .authorizeExchange()
                .pathMatchers("/allcourses")
                .hasAnyRole("USER", "ADMIN")
                .and()
            .authorizeExchange()
                .pathMatchers("/", "/login", "/signup", "/users/new", "/styles/**", "/images/**")
                .permitAll()
                .and()
            .formLogin()
                .loginPage("/login");

        return http.build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return (username) -> userRepository.findUserDetailsByUsername(username);
    }
}
