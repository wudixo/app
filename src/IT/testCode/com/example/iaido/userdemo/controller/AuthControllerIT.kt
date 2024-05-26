package com.example.iaido.userdemo.controller

import com.example.iaido.userdemo.AbstractIntegrationTest
import com.example.iaido.userdemo.model.dto.enum.SignUpErrors
import com.example.iaido.userdemo.util.RequestUtil
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql(scripts = ["/sql/controller/auth-controller-cleanup.sql", "/sql/controller/auth-controller-setup.sql"])
internal class AuthControllerIT : AbstractIntegrationTest() {

    @Test
    fun `check random call is check random call is unauthorized `() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()["v1/random/api"]
            .then()
            .assertThat()
            .statusCode(HttpStatus.UNAUTHORIZED.value()).log().body(true)
    }

    @Test
    fun `registerPerson  email already in use`() {

        RequestUtil.post(
            "v1/api/auth/signup",
            "json/controller/auth-controller/sign-up-request-already-active-username.json"
        ).assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("successfulAccountCreation", equalTo(false))
            .body("error", equalTo(SignUpErrors.ALREADY_USED_USERNAME.toString()))
            .body("signUpForm.username", equalTo("shane.sturgeon"))
            .body("signUpForm.password", equalTo("password"))
            .body("signUpForm.email", equalTo("shane.sturgeon@iaido.com"))
            .body("signUpForm.forename", equalTo("shane"))
            .body("signUpForm.surname", equalTo("sturgeon"))
            .body("signUpForm.phone", equalTo(23232342))
            .body("signUpForm.dateOfBirth", equalTo("11/01/1995"));
    }

    @Test
    fun `registerPerson  username already in use`() {

        RequestUtil.post(
            "v1/api/auth/signup",
            "json/controller/auth-controller/sign-up-request-already-active-email.json"
        ).assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("successfulAccountCreation", equalTo(false))
            .body("error", equalTo(SignUpErrors.ALREADY_USED_EMAIL.toString()))
            .body("signUpForm.username", equalTo("shane.the.username"))
            .body("signUpForm.password", equalTo("password"))
            .body("signUpForm.email", equalTo("shane.sturgeon@iaido.com"))
            .body("signUpForm.forename", equalTo("shane"))
            .body("signUpForm.surname", equalTo("sturgeon"))
            .body("signUpForm.phone", equalTo(23232342))
            .body("signUpForm.dateOfBirth", equalTo("11/01/1995"));
    }

    @Test
    fun `registerPerson  person created`() {

        RequestUtil.post(
            "v1/api/auth/signup",
            "json/controller/auth-controller/sign-up-request-new-person.json"
        ).assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("successfulAccountCreation", equalTo(true))
            .body("person.id", CoreMatchers.notNullValue())
            .body("person.forename", equalTo("bob"))
            .body("person.surname", equalTo("smith"))
            .body("person.email", equalTo("bob.smith@iaido.com"))
            .body("person.phone", equalTo(23232342))
            .body("person.dateOfBirth", equalTo("1995-01-11"))
            .body("person.username", equalTo("new.username"))
            .body("person.age", equalTo(27))
            .body("jwtToken", CoreMatchers.notNullValue());
    }

    @Test
    fun `authenticate incorrect password`() {

        RequestUtil.post(
            "v1/api/auth/authenticate",
            "json/controller/auth-controller/sign-in-request-wrong-password.json"
        ).assertThat()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `authenticate correct password`() {

        RequestUtil.post(
            "v1/api/auth/authenticate",
            "json/controller/auth-controller/sign-in-request-correct-password.json"
        ).assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("jwtToken", CoreMatchers.notNullValue())
    }
}
