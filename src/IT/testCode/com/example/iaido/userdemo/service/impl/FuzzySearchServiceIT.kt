package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.AbstractIntegrationTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql(scripts = ["/sql/service/fuzzy-search-service-cleanup.sql", "/sql/service/fuzzy-search-service-setup.sql"])
internal class FuzzySearchServiceIT : AbstractIntegrationTest() {

    @Autowired
    private lateinit var fuzzySearchServiceImpl: FuzzySearchServiceImpl;

    @Test
    fun `fuzzySearchOnFullName misspell name, match one person first name and anothers lastname`() {
        val peopleResponse = fuzzySearchServiceImpl.fuzzySearchOnFullName("smitg")

        Assertions.assertThat(peopleResponse.persons)
            .hasSize(2)
            .extracting("email").containsExactlyInAnyOrder("bob.smith@iaido.com", "smith.bob@iaido.com");

    }

    @Test
    fun `fuzzySearchOnFullName full name`() {
        val peopleResponse = fuzzySearchServiceImpl.fuzzySearchOnFullName("Booplop Larington")

        Assertions.assertThat(peopleResponse.persons)
            .hasSize(1)
            .extracting("email").containsExactlyInAnyOrder("booplop.Larington@iaido.com");

    }

    @Test
    fun `fuzzySearchOnFullName wrong character in first name`() {
        val peopleResponse = fuzzySearchServiceImpl.fuzzySearchOnFullName("Booplot Larington")

        Assertions.assertThat(peopleResponse.persons)
            .hasSize(1)
            .extracting("email").containsExactlyInAnyOrder("booplop.Larington@iaido.com");

    }


}

