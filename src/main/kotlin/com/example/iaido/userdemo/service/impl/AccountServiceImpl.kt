package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.model.auth.AuthPerson
import com.example.iaido.userdemo.model.dto.SignUpResponse
import com.example.iaido.userdemo.model.dto.enum.SignUpErrors
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.service.AccountService
import com.example.iaido.userdemo.service.JwtService
import com.example.iaido.userdemo.service.PersonService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AccountServiceImpl(
    val personService: PersonService,
    val passwordEncoder: PasswordEncoder,
    var authenticationManager: AuthenticationManager,
    var jwtService: JwtService
) :
    AccountService {

    override fun createPersonAccount(request: SignUpRequest): SignUpResponse {

        if (personService.personExistsByUsername(request.username)) {
            return SignUpResponse.Builder()
                .error(SignUpErrors.ALREADY_USED_USERNAME)
                .accountCreated(false)
                .signUpForm(request).build();
        }

        if (personService.personExistsByEmail(request.email)) {
            return SignUpResponse.Builder()
                .error(SignUpErrors.ALREADY_USED_EMAIL)
                .accountCreated(false)
                .signUpForm(request).build();
        }

        val password = passwordEncoder.encode(request.password);
        val person = personService.createPerson(request, password);

        return SignUpResponse.Builder()
            .accountCreated(true)
            .person(person)
            .build();
    }

    override fun loginAccountAndGetAuthentication(username: String, password: String): String {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().authentication = authentication;
        val authPerson = authentication.principal as AuthPerson
        return jwtService.generateJwtToken(authPerson.username);
    }
}
