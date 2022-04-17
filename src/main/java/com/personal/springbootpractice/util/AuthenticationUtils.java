package com.personal.springbootpractice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationUtils {

    public static Mono<String> authenticateEndpoint(String requestedEndpoint) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::isAuthenticated)
                .flatMap(loggedIn -> {
                    if(loggedIn) { return Mono.just("redirect:/"); }
                    return Mono.just(requestedEndpoint);
                });
    }

    public static Mono<Boolean> isAuthenticated() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::isAuthenticated)
                .flatMap(auth -> {
                    if(auth) return Mono.just(true);
                    return Mono.just(false);
                });
    }
}
