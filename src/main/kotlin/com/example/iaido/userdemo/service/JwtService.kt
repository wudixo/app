package com.example.iaido.userdemo.service

interface JwtService {

    fun generateJwtToken(username: String): String

    fun getUserNameFromJwtToken(token: String): String

    fun validateJwtToken(authToken: String): Boolean
}
