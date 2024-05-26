package com.example.iaido.userdemo.service

import com.example.iaido.userdemo.AbstractIntegrationTest
import com.example.iaido.userdemo.model.dto.form.PersonSearchForm
import com.example.iaido.userdemo.model.repo.enum.SearchFields
import com.example.iaido.userdemo.model.repo.enum.SearchType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.context.jdbc.Sql


@Sql(scripts = ["/sql/service/person-service/cleanup.sql", "/sql/service/person-service/setup.sql"])
internal class PersonServiceIT() : AbstractIntegrationTest() {

    @Autowired
    private lateinit var personService: PersonService;

    @Test
    fun `getPerson use default search params and get single result on forename`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(forename = "shane", age = 55);
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(1);
        Assertions.assertThat(persons.get().toList()).extracting("forename").containsExactly("shane")
    }

    @Test
    fun `getPerson use default search params and get multiple result on forename`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(forename = "to", age = 55);
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(3);
        Assertions.assertThat(persons.get().toList()).extracting("forename")
            .containsExactlyInAnyOrder("tommy", "tod", "toffy");
    }

    @Test
    fun `getPerson use default search params and get multiple result on age`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(forename = "z", age = 44);
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(2);
        Assertions.assertThat(persons.get().toList()).extracting("forename")
            .containsExactlyInAnyOrder("tommy", "shane");
    }

    @Test
    fun `getPerson use default search fields with and search type to return single result`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(forename = "shane", age = 44, searchType = SearchType.AND);
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(1);
        Assertions.assertThat(persons.get().toList()).extracting("forename")
            .containsExactlyInAnyOrder("shane");
    }

    @Test
    fun `getPerson search on forename and surname`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(
            forename = "tod",
            surname = "sturgeon",
            searchType = SearchType.AND,
            searchFields = arrayListOf(SearchFields.SURNAME, SearchFields.FORENAME)
        );
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(1);
        Assertions.assertThat(persons.get().toList()).extracting("forename")
            .containsExactlyInAnyOrder("tod");
    }

    @Test
    fun `getPerson search on forename or surname`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(
            forename = "tod",
            surname = "sturgeon",
            searchType = SearchType.OR,
            searchFields = arrayListOf(SearchFields.SURNAME, SearchFields.FORENAME)
        );
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(4);
        Assertions.assertThat(persons.get().toList()).extracting("forename")
            .containsExactlyInAnyOrder("tommy", "tod", "toffy", "shane");
    }

    @Test
    fun `getPerson search on partial forename AND partial surname AND age AND partial email`() {
        val pageable = Pageable.unpaged()
        val personSearchForm = PersonSearchForm(
            forename = "to",
            surname = "stur",
            email = "iaido.com",
            age = 12,
            searchType = SearchType.AND,
            searchFields = arrayListOf(
                SearchFields.SURNAME,
                SearchFields.FORENAME,
                SearchFields.AGE,
                SearchFields.EMAIL
            )
        );
        val persons = personService.getPerson(personSearchForm, pageable);

        Assertions.assertThat(persons.totalElements).isEqualTo(1);
        Assertions.assertThat(persons.get().toList()).extracting("forename")
            .containsExactlyInAnyOrder("tod");
    }
}
