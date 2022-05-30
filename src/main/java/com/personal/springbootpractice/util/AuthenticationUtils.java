package com.personal.springbootpractice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationUtils {

    /**
     * @param requestedEndpoint The endpoint being requested by the user
     * @return A Mono containing a string which will either serve the requested page if the user is not logged in, or
     * the home page if the user is already logged in
     */
    public static Mono<String> authenticateEndpoint(String requestedEndpoint) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::isAuthenticated)
                .flatMap(loggedIn -> {
                    if(loggedIn) { return Mono.just("redirect:/"); }
                    return Mono.just(requestedEndpoint);
                });
    }

    /**
     * @return A Mono containing true if the user is logged in or false if the user is not
     */
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
