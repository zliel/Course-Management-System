package com.personal.springbootpractice;

import com.personal.springbootpractice.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;

@DataMongoTest
public class MongoDBTests {

    @Autowired
    MongoOperations ops;

    @BeforeEach
    void setup() {

    }
}
