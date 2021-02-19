package com.udacity.jdnd.course3.critter.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String name;

//    @OneToMany
//    private List<Schedule> scheduleList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
