package com.example.iaido.userdemo.controller

import com.example.iaido.userdemo.model.auth.AuthPerson
import com.example.iaido.userdemo.model.dto.AuthenticatedResponse
import com.example.iaido.userdemo.model.dto.form.SignInRequest
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/api/auth")
class AuthController(
    val accountService: AccountService
) {

    @PostMapping("/signup")
    fun registerPerson(@RequestBody signupRequest: SignUpRequest): ResponseEntity<Any> {

        val singUpResponse = accountService.createPersonAccount(signupRequest);
        if (singUpResponse.successfulAccountCreation == true) {
            singUpResponse.jwtToken = accountService.loginAccountAndGetAuthentication(signupRequest.username, signupRequest.password);
        }

        return ResponseEntity.ok<Any>(singUpResponse)
    }

    @PostMapping("/authenticate")
    fun authenticatePerson(@RequestBody signInRequest: SignInRequest): ResponseEntity<Any> {
        val jwtToken = accountService.loginAccountAndGetAuthentication(signInRequest.username, signInRequest.password);

        val loggedInPerson = SecurityContextHolder.getContext().authentication.principal as AuthPerson;
        return ResponseEntity.ok<Any>(AuthenticatedResponse(jwtToken, loggedInPerson));
    }
}
