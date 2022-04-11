package com.personal.springbootpractice.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document
public class Course implements Comparable<Course> {

    @Id
    private String id;
    private String name;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    
    private int maxStudents;

    public Course(String courseName, Date startDate, Date endDate, int maxStudents) {
        this.id = new ObjectId().toString();
        this.name = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
    }

    public Course() {
        this.id = new ObjectId().toString();
    }

    @Override
    public int compareTo(Course o) {
        return this.id.compareTo(o.id);
    }
}
