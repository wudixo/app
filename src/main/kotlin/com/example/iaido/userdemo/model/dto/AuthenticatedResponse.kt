package com.example.iaido.userdemo.model.dto

import com.example.iaido.userdemo.model.auth.AuthPerson

class AuthenticatedResponse(val jwtToken: String, authPerson: AuthPerson) {
}
