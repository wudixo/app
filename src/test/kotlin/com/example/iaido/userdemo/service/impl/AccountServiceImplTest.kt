package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.model.dto.enum.SignUpErrors
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.service.JwtService
import com.example.iaido.userdemo.service.PersonService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class AccountServiceImplTest {

    @InjectMocks
    private lateinit var accountServiceImpl: AccountServiceImpl;

    @Mock
    private lateinit var personService: PersonService;

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder;

    @Mock
    private lateinit var authenticationManager: AuthenticationManager;

    @Mock
    private lateinit var jwtService: JwtService;

    @Test
    fun `createPersonAccount username already used in system`() {
        val signUpRequest =
            SignUpRequest("shane.sturgeon", "password", "email", "shane", "sturgeon", 4545454L, LocalDate.now());

        `when`(personService.personExistsByUsername("shane.sturgeon")).thenReturn(true);

        val signUpResponse = accountServiceImpl.createPersonAccount(signUpRequest);

        Assertions.assertThat(signUpResponse.error).isEqualTo(SignUpErrors.ALREADY_USED_USERNAME);
        Assertions.assertThat(signUpResponse.successfulAccountCreation).isEqualTo(false);
        Assertions.assertThat(signUpResponse.signUpForm).isEqualTo(signUpRequest);
        Assertions.assertThat(signUpResponse.jwtToken).isNull();
        Assertions.assertThat(signUpResponse.person).isNull();
    }

    @Test
    fun `createPersonAccount email already used in system`() {
        val signUpRequest =
            SignUpRequest("shane.sturgeon", "password", "email", "shane", "sturgeon", 4545454L, LocalDate.now());

        `when`(personService.personExistsByUsername("shane.sturgeon")).thenReturn(false);
        `when`(personService.personExistsByEmail("email")).thenReturn(true);

        val signUpResponse = accountServiceImpl.createPersonAccount(signUpRequest);

        Assertions.assertThat(signUpResponse.error).isEqualTo(SignUpErrors.ALREADY_USED_EMAIL);
        Assertions.assertThat(signUpResponse.successfulAccountCreation).isEqualTo(false);
        Assertions.assertThat(signUpResponse.signUpForm).isEqualTo(signUpRequest);
        Assertions.assertThat(signUpResponse.jwtToken).isNull();
        Assertions.assertThat(signUpResponse.person).isNull();
    }

    @Test
    fun `createPersonAccount create person`() {
        val signUpRequest =
            SignUpRequest("shane.sturgeon", "password", "email", "shane", "sturgeon", 4545454L, LocalDate.now());

        val encodedPassword = "thisIsEncodedPassword";
        val person = Person(
            forename = "shane",
            surname = "sturgeon",
            email = "email",
            username = "shane.sturgeon",
            password = "secret",
            phone = 4545454L,
            dateOfBirth = LocalDate.now()
        );

        `when`(personService.personExistsByUsername("shane.sturgeon")).thenReturn(false);
        `when`(personService.personExistsByEmail("email")).thenReturn(false);
        `when`(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        `when`(personService.createPerson(signUpRequest, encodedPassword)).thenReturn(person);

        val signUpResponse = accountServiceImpl.createPersonAccount(signUpRequest);

        Assertions.assertThat(signUpResponse.successfulAccountCreation).isEqualTo(true);
        Assertions.assertThat(signUpResponse.person).isEqualTo(person);
        Assertions.assertThat(signUpResponse.signUpForm).isNull();
        Assertions.assertThat(signUpResponse.jwtToken).isNull();
        Assertions.assertThat(signUpResponse.error).isNull();
    }
}
