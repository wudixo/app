package com.example.iaido.userdemo.model.dto

import com.example.iaido.userdemo.model.entity.Person
import java.time.LocalDate

class PersonDto(person: Person) {

    val forename: String;
    val surname: String;
    val dateOfBirth: LocalDate;
    val age: Int;
    val email: String;

    init {
        this.forename = person.forename;
        this.surname = person.surname
        this.dateOfBirth = person.dateOfBirth
        this.age = person.age
        this.email = person.email;
    }
}
