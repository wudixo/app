package com.example.iaido.userdemo.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.example.iaido.userdemo.model.dto.enum.SignUpErrors
import com.example.iaido.userdemo.model.dto.form.SignUpRequest
import com.example.iaido.userdemo.model.entity.Person

@JsonInclude(JsonInclude.Include.NON_NULL)
class SignUpResponse private constructor(
    val successfulAccountCreation: Boolean?,
    val error: SignUpErrors?,
    val signUpForm: SignUpRequest?,
    val person: Person?,
    var jwtToken: String?,
) {

    data class Builder(
        var successfulAccountCreation: Boolean? = null,
        var error: SignUpErrors? = null,
        var signUpForm: SignUpRequest? = null,
        var person: Person? = null,
        var jwtToken: String? = null
    ) {
        fun accountCreated(accountCreated: Boolean) = apply { this.successfulAccountCreation = accountCreated }
        fun error(singUpError: SignUpErrors) = apply { this.error = singUpError }
        fun signUpForm(signUpForm: SignUpRequest) = apply { this.signUpForm = signUpForm }
        fun person(person: Person) = apply { this.person = person }
        fun jwtToken(jwtToken: String) = apply { this.jwtToken = jwtToken }
        fun build() = SignUpResponse(successfulAccountCreation, error, signUpForm, person, jwtToken)
    }

}
