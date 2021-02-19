package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.enums.EmployeeSkill;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
public class Employee extends User{

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> skills;

    @ElementCollection
    private Set<DayOfWeek> daysAvailable;

    @ManyToMany(mappedBy = "employees")
    Set<Schedule> schedules;

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }
}
