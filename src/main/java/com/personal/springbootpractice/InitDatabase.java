package com.personal.springbootpractice;

import com.personal.springbootpractice.models.Course;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class InitDatabase {

    @Bean
    public CommandLineRunner initialize(MongoOperations ops) {
        return args -> {
            ops.insert(new Course("Newly Made Course!", new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30, "UCSD"));
            ops.insert(new Course( "Newly Made Course 2.0!", new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30, "UCSD"));
            ops.insert(new Course("This is an old course lol!", new Date(), Date.from(Instant.now().plus(90, ChronoUnit.DAYS)), 30, "UCSD"));
            
            ops.findAll(Course.class).forEach(System.out::println);
        };
    }
}
