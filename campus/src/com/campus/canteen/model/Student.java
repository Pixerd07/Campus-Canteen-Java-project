package com.campus.canteen.model;
// student information
public class Student { // class
    private String name;
    private long id;

    // when create a new student  object

    public Student(String name , long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

}
