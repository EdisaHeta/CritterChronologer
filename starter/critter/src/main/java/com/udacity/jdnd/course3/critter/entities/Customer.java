package com.udacity.jdnd.course3.critter.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
public class Customer extends User{
    private String phoneNumber;
    private String notes;

    @OneToMany(mappedBy = "owner")
    private Set<Pet> pets;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }
}
