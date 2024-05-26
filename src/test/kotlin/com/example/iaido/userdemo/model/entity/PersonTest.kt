package com.example.iaido.userdemo.model.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class PersonTest {

    @Test
    fun `person age generated from DoB if not provided`() {
        val yearsAgoDoB = 55L;
        val dateOfBirth = LocalDate.now().minusYears(yearsAgoDoB);
        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = dateOfBirth
        );

        Assertions.assertThat(person.age).isEqualTo(yearsAgoDoB);
    }

    @Test
    fun `person age not generated if DoB provided`() {
        val passedInDobYearsAgo = 55L;
        val constructorAge = 77;
        val dateOfBirth = LocalDate.now().minusYears(passedInDobYearsAgo)

        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = dateOfBirth,
            age = constructorAge
        );

        Assertions.assertThat(person.age).isNotEqualTo(passedInDobYearsAgo);
        Assertions.assertThat(person.age).isEqualTo(constructorAge);
    }

    @Test
    fun `person non empty UUID`() {
        val dateOfBirth = LocalDate.now().minusYears(55);
        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = dateOfBirth
        );

        Assertions.assertThat(person.id).isNotEqualTo("");
    }
}
