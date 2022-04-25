package com.personal.springbootpractice.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class School {

    @Id
    public String id;
    public String name;

    public School(String name) {
        this.id = new ObjectId().toString();
        this.name = name;
    }

    public School() { }
}
