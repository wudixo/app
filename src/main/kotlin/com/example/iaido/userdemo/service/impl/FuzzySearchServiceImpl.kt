package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.model.dto.PersonResponse
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.service.FuzzySearchService
import org.hibernate.search.jpa.Search
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
class FuzzySearchServiceImpl(val entityManager: EntityManager) : FuzzySearchService {

    /**
     * Fuzzy search on field fullName that pulls in and combines forename and surname, with a small edit distance so
     * only show minor fields that closly match.
     */
    @Transactional
    override fun fuzzySearchOnFullName(nameToSearch: String): PersonResponse {
        val fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();

        val queryBuilder = fullTextEntityManager.searchFactory.buildQueryBuilder().forEntity(Person::class.java).get();
        val fullNameQuery = queryBuilder.keyword()
            .fuzzy()
            .withEditDistanceUpTo(2)
            .onField("fullName")
            .matching(nameToSearch)
            .createQuery();

        val fullTextQuery = fullTextEntityManager.createFullTextQuery(fullNameQuery, Person::class.java);
        val personList = fullTextQuery.resultList as List<Person>
        return PersonResponse(personList, success = true);
    }
}
