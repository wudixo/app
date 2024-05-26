package com.example.iaido.userdemo.util

import io.restassured.RestAssured.given
import io.restassured.response.ValidatableResponse

class RequestUtil {
    companion object {

        fun post(endpoint: String, requestBodyFile: String): ValidatableResponse {
            val file = FileUtil().getFile(requestBodyFile);

            return given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(file)
                .`when`()
                .post(endpoint)
                .then()
                .log().all(true)
        }

        fun get(endpoint: String): ValidatableResponse {
            return given()
                .header("Content-Type", "application/json")
                .`when`()
                .get(endpoint)
                .then()
                .log().all(true)
        }

        fun getNoContentType(endpoint: String): ValidatableResponse {
            return given()
                .`when`()
                .get(endpoint)
                .then()
                .log().all(true)
        }

        fun get(endpoint: String, jwtToken: String): ValidatableResponse {
            return given()
                .header("Content-Type", "application/json")
                .header("authorization", "Bearer $jwtToken")
                .`when`()
                .get(endpoint)
                .then()
                .log().all(true)
        }

        fun delete(endpoint: String, jwtToken: String): ValidatableResponse {
            return given()
                .header("Content-Type", "application/json")
                .header("authorization", "Bearer $jwtToken")
                .`when`()
                .delete(endpoint)
                .then()
                .log().all(true)
        }

        fun patch(endpoint: String, jwtToken: String): ValidatableResponse {
            return given()
                .header("Content-Type", "application/json")
                .header("authorization", "Bearer $jwtToken")
                .`when`()
                .patch(endpoint)
                .then()
                .log().all(true)
        }
    }
}
