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
                .pathMatchers("/users/delete", "/users/delete/**", "/schools/delete", "/schools/delete/**", "/api/schools/delete/**", "/api/users/**", "/api/courses")
                .hasRole("ADMIN")
                .and()
            .authorizeExchange()
                .pathMatchers("/allcourses", "/courses/**", "/api/courses/school=**", "/api/courses/new/**", "/swagger-ui/*", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**")
                .hasAnyRole("USER", "ADMIN")
                .and()
            .authorizeExchange()
                .pathMatchers("/", "/login", "/signup", "/users/new", "/schools/new", "/styles/**", "/images/**")
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
