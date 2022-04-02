package com.personal.springbootpractice.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private String schoolName;

    public User(String username, String password, String schoolName) {
        this.id = new ObjectId().toString();
        this.username = username;
        this.password = password;
        this.schoolName = schoolName;
    }

    public User() {
        this.id = new ObjectId().toString();
    }

}
