package com.campus.canteen.model;
// student information
public class Student { // class
    private String name;
    private long id;

    // when create a new student  object

    public Student(String name , long id) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than zero.");
        }
        this.name = name.trim();
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

}
