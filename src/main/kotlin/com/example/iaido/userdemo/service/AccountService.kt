package com.example.iaido.userdemo.service

import com.example.iaido.userdemo.model.dto.SignUpResponse
import com.example.iaido.userdemo.model.dto.form.SignUpRequest

interface AccountService {

    fun createPersonAccount(request: SignUpRequest) : SignUpResponse

    fun loginAccountAndGetAuthentication(username: String, password: String) : String
}
