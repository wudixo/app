package com.example.iaido.userdemo.service

import com.example.iaido.userdemo.model.dto.PersonResponse
import com.example.iaido.userdemo.model.dto.form.PersonSearchForm
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.model.entity.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface PersonService {

    fun getPerson(pesonSearchForm: PersonSearchForm, pageable: Pageable): Page<Person>

    fun personExistsByUsername(username: String): Boolean

    fun personExistsByEmail(email: String): Boolean

    fun createPerson(request: SignUpRequest, encryptedPassword: String): Person

    fun getPerson(personId: String): Optional<Person>;

    fun deletePerson(personId: String): PersonResponse;

    fun upgradeGuestToAdmin(personId: String): PersonResponse;

    fun getAllPeoplePaged(pageable: Pageable): Page<Person>;

    fun getNumberOfPeople(): Long;

    fun getNumberOfPeopleByRole(role: String): Long

}
