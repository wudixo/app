package com.example.iaido.userdemo.model.dto.form

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

class SignUpRequest(
    val username: String,
    val password: String,
    val email: String,
    val forename: String,
    val surname: String,
    val phone: Long,
    @JsonFormat(pattern = "dd/MM/yyyy")
    val dateOfBirth: LocalDate
) {

}
