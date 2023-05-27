package entities;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;

@Entity(name = "students")
public class Student {
    @Id
    private long id;

    @Column(name = "name")
    private String name;

    public Student(String name) {
        this.name = name;
    }

    public Student() {};
}
