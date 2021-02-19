package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.enums.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Schedule {

    @Id
    @GeneratedValue
    private long id;

    private LocalDate date;

    @ElementCollection
    private Set<EmployeeSkill> activities;

    @ManyToMany
    @JoinTable(
            name = "pet_schedule",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    Set<Pet> pets;

    @ManyToMany
    @JoinTable(
            name = "employee_schedule",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    Set<Employee> employees;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<EmployeeSkill> getActivities() {
        return activities;
    }

    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
