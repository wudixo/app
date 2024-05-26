package com.example.iaido.userdemo.controller

import com.example.iaido.userdemo.model.dto.PersonResponse
import com.example.iaido.userdemo.model.dto.enum.PersonError
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.service.FuzzySearchService
import com.example.iaido.userdemo.service.PersonService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class PersonControllerTest {

    @InjectMocks
    private lateinit var personController: PersonController

    @Mock
    private lateinit var personService: PersonService

    @Mock
    private lateinit var fuzzySearchService: FuzzySearchService

    @Test
    fun `getPerson returned no results`() {
        `when`(personService.getPerson("personId")).thenReturn(Optional.empty())

        val response = personController.getPerson("personId").body as PersonResponse;

        Assertions.assertThat(response.persons).isNull();
        Assertions.assertThat(response.success).isFalse;
        Assertions.assertThat(response.personError).isEqualTo(PersonError.NO_RESULTS_FOUND_FOR_ID);
    }

    @Test
    fun `getPerson returned results`() {
        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = LocalDate.now()
        );

        `when`(personService.getPerson("personId")).thenReturn(Optional.of(person))

        val response = personController.getPerson("personId").body as PersonResponse;

        Assertions.assertThat(response.persons).hasSize(1).containsExactly(person);
        Assertions.assertThat(response.success).isTrue;
        Assertions.assertThat(response.personError).isNull()
    }
}
