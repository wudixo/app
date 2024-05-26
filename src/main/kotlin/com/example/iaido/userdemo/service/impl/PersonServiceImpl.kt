package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.model.auth.AuthPerson
import com.example.iaido.userdemo.model.dto.PersonResponse
import com.example.iaido.userdemo.model.dto.enum.PersonError
import com.example.iaido.userdemo.model.dto.form.PersonSearchForm
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.model.entity.enum.AuthRoles
import com.example.iaido.userdemo.model.repo.PersonSpecification
import com.example.iaido.userdemo.repo.PersonRepository
import com.example.iaido.userdemo.service.PersonService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonServiceImpl(val personRepo: PersonRepository) : PersonService,
    UserDetailsService {

    override fun getPerson(personSearchForm: PersonSearchForm, pageable: Pageable): Page<Person> {
        return personRepo.findAll(PersonSpecification(personSearchForm), pageable);
    }

    override fun getPerson(personId: String): Optional<Person> {
        return personRepo.findById(personId);
    }

    override fun personExistsByUsername(username: String): Boolean {
        return personRepo.existsByUsername(username);
    }

    override fun personExistsByEmail(email: String): Boolean {
        return personRepo.existsByEmail(email);
    }

    override fun createPerson(request: SignUpRequest, encryptedPassword: String): Person {
        val person = Person(
            forename = request.forename,
            dateOfBirth = request.dateOfBirth,
            phone = request.phone,
            email = request.email,
            password = encryptedPassword,
            surname = request.surname,
            username = request.username,
        );
        person.role = AuthRoles.GUEST.name;
        personRepo.save(person);

        return person;
    }

    /**
     * Delete a person by passing in the UUID String
     *
     *  @return PersonResponse object that contains errors if occurred, NO_RESULTS_FOUND_FOR_ID if iD
     *  does not exist in the system, COULD_NOT_DELETE_USER if the user still exists after trying to delete
     *  and success set to true if all is deleted correctly
     */
    override fun deletePerson(personId: String): PersonResponse {
        val boolean = personRepo.existsById(personId);
        if (!boolean) {
            return PersonResponse(personError = PersonError.NO_RESULTS_FOUND_FOR_ID);

        }
        personRepo.deleteById(personId);
        val stillFound = personRepo.existsById(personId);
        if (stillFound) {
            return PersonResponse(personError = PersonError.COULD_NOT_DELETE_USER);
        }
        return PersonResponse(success = true);
    }

    override fun upgradeGuestToAdmin(personId: String): PersonResponse {
        val personExist = personRepo.existsById(personId);
        if (!personExist) {
            return PersonResponse(personError = PersonError.NO_RESULTS_FOUND_FOR_ID);
        }

        val person = personRepo.getById(personId);
        if (AuthRoles.ADMIN == person.getRole()) {
            return PersonResponse(personError = PersonError.CANNOT_UPGRADE_PERSON_ALREADY_ADMIN);
        }

        person.role = AuthRoles.ADMIN.name;
        personRepo.save(person);
        return PersonResponse(success = true, persons = listOf(person));
    }

    override fun getAllPeoplePaged(pageable: Pageable): Page<Person> {
        return personRepo.findAll(pageable);
    }

    override fun getNumberOfPeople(): Long {
        return personRepo.count();
    }

    override fun getNumberOfPeopleByRole(role: String): Long {
       return personRepo.countAllByRole(role);
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val person = personRepo.findByUsername(username);
        return AuthPerson.build(person);
    }

}
