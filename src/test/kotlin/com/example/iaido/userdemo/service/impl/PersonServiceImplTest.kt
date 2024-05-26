package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.model.dto.enum.PersonError
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.model.entity.enum.AuthRoles
import com.example.iaido.userdemo.repo.PersonRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class PersonServiceImplTest {

    @InjectMocks
    private lateinit var personService: PersonServiceImpl;

    @Mock
    private lateinit var personRepo: PersonRepository;

    @Test
    fun `deletePerson id not found`() {
        val uuidString = "123uuid";

        `when`(personRepo.existsById(uuidString)).thenReturn(false);
        val personResponse = personService.deletePerson(uuidString);

        verify(personRepo, never()).deleteById(uuidString);

        Assertions.assertThat(personResponse.success).isFalse;
        Assertions.assertThat(personResponse.personError).isEqualTo(PersonError.NO_RESULTS_FOUND_FOR_ID);
    }

    @Test
    fun `deletePerson could not delete user`() {
        val uuidString = "123uuid";

        `when`(personRepo.existsById(uuidString)).thenReturn(true);
        val personResponse = personService.deletePerson(uuidString);

        verify(personRepo, times(1)).deleteById(uuidString);
        verify(personRepo, times(2)).existsById(uuidString);

        Assertions.assertThat(personResponse.success).isFalse;
        Assertions.assertThat(personResponse.personError).isEqualTo(PersonError.COULD_NOT_DELETE_USER);
    }

    @Test
    fun `deletePerson success`() {
        val uuidString = "123uuid";

        `when`(personRepo.existsById(uuidString)).thenReturn(true).thenReturn(false);
        val personResponse = personService.deletePerson(uuidString);

        verify(personRepo, times(1)).deleteById(uuidString);

        Assertions.assertThat(personResponse.success).isTrue;
        Assertions.assertThat(personResponse.personError).isNull();
    }

    @Test
    fun `upgradePerson already admin`() {
        val uuid = "123uuid";

        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = LocalDate.now()
        );
        person.role = AuthRoles.ADMIN.name;

        `when`(personRepo.getById(uuid)).thenReturn(person);
        `when`(personRepo.existsById(uuid)).thenReturn(true);

        val personResponse = personService.upgradeGuestToAdmin(uuid);

        verify(personRepo, never()).save(person);

        Assertions.assertThat(personResponse.success).isFalse;
        Assertions.assertThat(personResponse.personError).isEqualTo(PersonError.CANNOT_UPGRADE_PERSON_ALREADY_ADMIN);
    }

    @Test
    fun `upgradePerson no person`() {
        val uuid = "123uuid";

        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = LocalDate.now()
        );
        person.role = AuthRoles.ADMIN.name;

        `when`(personRepo.existsById(uuid)).thenReturn(false);

        val personResponse = personService.upgradeGuestToAdmin(uuid);

        verify(personRepo, never()).save(person);
        verify(personRepo, never()).getById(uuid);

        Assertions.assertThat(personResponse.success).isFalse;
        Assertions.assertThat(personResponse.personError).isEqualTo(PersonError.NO_RESULTS_FOUND_FOR_ID);
    }

    @Test
    fun `upgradePerson success`() {
        val uuid = "123uuid";

        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "shane.sturgeon@live.co.uk",
            username = "shane.sturgeon",
            password = "secret",
            phone = 99993993,
            dateOfBirth = LocalDate.now()
        );
        person.role = AuthRoles.GUEST.name;

        `when`(personRepo.getById(uuid)).thenReturn(person);
        `when`(personRepo.existsById(uuid)).thenReturn(true);

        val personResponse = personService.upgradeGuestToAdmin(uuid);

        verify(personRepo, times(1)).save(person);

        Assertions.assertThat(personResponse.success).isTrue;
        Assertions.assertThat(personResponse.persons).containsExactly(person);
    }

    @Test
    fun `createPerson with guest role`() {
        val signUpRequest = SignUpRequest(
            "shane.sturgeon",
            "password",
            "shane@email.com",
            "shane",
            "Sturgeon",
            6656844857,
            LocalDate.now()
        );
        val person = personService.createPerson(signUpRequest, "encrypedPassword")

        verify(personRepo, times(1)).save(any(Person::class.java))

        Assertions.assertThat(person.role).isEqualTo(AuthRoles.GUEST.toString())
        Assertions.assertThat(person.age).isEqualTo(0)
        Assertions.assertThat(person.password).isEqualTo("encrypedPassword")
        Assertions.assertThat(person.email).isEqualTo("shane@email.com")
        Assertions.assertThat(person.forename).isEqualTo("shane")
        Assertions.assertThat(person.surname).isEqualTo("Sturgeon")
    }

}
