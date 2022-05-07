package com.personal.springbootpractice;

import com.personal.springbootpractice.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    final
    UserRepository userRepository;

    public WebSecurityConfig(UserRepository userRepository) { this.userRepository = userRepository; }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/users/delete", "/users/delete/**",  "/api/users/**", "/schools/delete", "/schools/delete/**", "/api/schools", "/api/schools/delete/**", "/api/schools/edit", "/api/schools/id=", "/api/schools/name=", "/api/courses")
                .hasRole("ADMIN")
                .and()
            .authorizeExchange()
                .pathMatchers("/allcourses", "/courses/**", "/api/courses/school=**", "/api/courses/new/**", "/api/courses/edit", "/api/courses/delete/**", "/api/schools/new/**", "/swagger-ui/*", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**")
                .hasAnyRole("USER", "ADMIN")
                .and()
            .authorizeExchange()
                .pathMatchers("/", "/login", "/signup", "/users/new", "/schools/new", "/styles/**", "/images/**", "/favicon.ico")
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
