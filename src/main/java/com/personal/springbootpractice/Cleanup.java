package com.personal.springbootpractice;

import com.personal.springbootpractice.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;

@Component
public class Cleanup {

    final MongoOperations ops;

    public Cleanup(MongoOperations ops) {
        this.ops = ops;
    }

    @PreDestroy
    public void cleanDB() {
        Query queryOne = new Query().addCriteria(Criteria.where("courseName").is("Newly Made Course!"));
        Query queryTwo = new Query().addCriteria(Criteria.where("courseName").is("Newly Made Course 2.0!"));
        Query queryThree = new Query().addCriteria(Criteria.where("courseName").is("This is an old course lol!"));

        ArrayList<Course> deletedCourses = new ArrayList<>();
        deletedCourses.addAll(ops.findAllAndRemove(queryOne, "course"));
        deletedCourses.addAll(ops.findAllAndRemove(queryTwo, "course"));
        deletedCourses.addAll(ops.findAllAndRemove(queryThree, "course"));

        System.out.println(deletedCourses);
    }
}
