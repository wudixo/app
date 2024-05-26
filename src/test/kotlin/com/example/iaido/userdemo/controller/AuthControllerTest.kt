package com.example.iaido.userdemo.controller

import com.example.iaido.userdemo.model.dto.SignUpResponse
import com.example.iaido.userdemo.model.dto.enum.SignUpErrors
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.service.impl.AccountServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class AuthControllerTest {

    @InjectMocks
    private lateinit var authController: AuthController;

    @Mock
    private lateinit var accountService: AccountServiceImpl;

    @Test
    fun `registerPerson errored on account creation, don't authenticate account`() {
        val signUpRequest = SignUpRequest(
            username = "shane.sturgeon",
            password = "password",
            email = "email",
            forename = "shane",
            surname = "sturgeon",
            phone = 4545454L,
            dateOfBirth = LocalDate.now()
        );

        val signUpResponseBuilder = SignUpResponse.Builder()
            .signUpForm(signUpRequest)
            .accountCreated(false)
            .error(SignUpErrors.ALREADY_USED_EMAIL);

        `when`(accountService.createPersonAccount(signUpRequest)).thenReturn(signUpResponseBuilder.build());

        val response = authController.registerPerson(signUpRequest).body as SignUpResponse;

        verify(accountService, never()).loginAccountAndGetAuthentication(
            signUpRequest.username,
            signUpRequest.password
        );

        Assertions.assertThat(response.successfulAccountCreation).isFalse;
        Assertions.assertThat(response.person).isNull();
        Assertions.assertThat(response.error).isEqualTo(SignUpErrors.ALREADY_USED_EMAIL);
        Assertions.assertThat(response.jwtToken).isNull();
        Assertions.assertThat(response.signUpForm).isEqualTo(signUpRequest);

    }

    @Test
    fun `registerPerson successful account creation, authenticate`() {
        val signUpRequest = SignUpRequest(
            username = "shane.sturgeon",
            password = "password",
            email = "email",
            forename = "shane",
            surname = "sturgeon",
            phone = 4545454L,
            dateOfBirth = LocalDate.now()
        );

        val person = Person(
            surname = "sturgeon",
            forename = "sturgeon",
            username = "shane.sturgeon",
            password = "thisIsEncrypedPassword",
            phone = 4545454L,
            dateOfBirth = LocalDate.now(),
            email = "email"
        )

        val signUpResponseBuilder = SignUpResponse.Builder()
            .accountCreated(true)
            .person(person)

        `when`(accountService.createPersonAccount(signUpRequest)).thenReturn(signUpResponseBuilder.build());
        `when`(accountService.loginAccountAndGetAuthentication("shane.sturgeon", "password")).thenReturn("jwtToken")

        val response = authController.registerPerson(signUpRequest).body as SignUpResponse;
        Assertions.assertThat(response.successfulAccountCreation).isTrue;
        Assertions.assertThat(response.person).isEqualTo(person);
        Assertions.assertThat(response.error).isNull();
        Assertions.assertThat(response.jwtToken).isEqualTo("jwtToken");
        Assertions.assertThat(response.signUpForm).isNull();

    }
}
