package com.example.iaido.userdemo.controller

import com.example.iaido.userdemo.model.dto.PersonDto
import com.example.iaido.userdemo.model.dto.PersonResponse
import com.example.iaido.userdemo.model.dto.enum.PersonError
import com.example.iaido.userdemo.model.dto.form.PersonSearchForm
import com.example.iaido.userdemo.service.FuzzySearchService
import com.example.iaido.userdemo.service.PersonService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/api/person")
class PersonController(var personService: PersonService, var fuzzySearchService: FuzzySearchService) {

    @GetMapping("/{personId}")
    fun getPerson(@PathVariable personId: String): ResponseEntity<Any> {

        val personOptional = personService.getPerson(personId);
        if (personOptional.isPresent) {
            return ResponseEntity.ok(PersonResponse(persons = arrayListOf(personOptional.get()), success = true));
        }
        return ResponseEntity.ok(PersonResponse(personError = PersonError.NO_RESULTS_FOUND_FOR_ID));
    }

    @DeleteMapping("/remove/{personId}")
    fun deletePerson(@PathVariable personId: String): ResponseEntity<Any> {
        val personResponse = personService.deletePerson(personId);
        return ResponseEntity.ok(personResponse);
    }

    @PatchMapping("/upgrade/{personId}")
    fun upgradePerson(@PathVariable personId: String): ResponseEntity<Any> {
        val personResponse = personService.upgradeGuestToAdmin(personId);
        return ResponseEntity.ok(personResponse);
    }

    @GetMapping("/all")
    fun getAllPeoplePaged(pageable: Pageable): ResponseEntity<Any> {
        val personResponse = personService.getAllPeoplePaged(pageable);
        return ResponseEntity.ok(personResponse);
    }

    @GetMapping("/shared/search")
    fun searchPerson(personSearchForm: PersonSearchForm, pageable: Pageable): ResponseEntity<Any> {
        val pagedSearchResults = personService.getPerson(personSearchForm, pageable);
        val pagedPersonDtoResults = pagedSearchResults.map { person -> PersonDto(person) };
        return ResponseEntity.ok(pagedPersonDtoResults);
    }

    @GetMapping("name-search")
    fun fuzzyFullNameSearch(name: String): ResponseEntity<Any> {
        val personResponse = fuzzySearchService.fuzzySearchOnFullName(name);
        return ResponseEntity.ok(personResponse);
    }

}
