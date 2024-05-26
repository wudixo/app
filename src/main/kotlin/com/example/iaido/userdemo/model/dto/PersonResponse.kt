package com.example.iaido.userdemo.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.example.iaido.userdemo.model.dto.enum.PersonError
import com.example.iaido.userdemo.model.entity.Person

/**
 *  probably  abit overkill but could be used if there is endpoints to retrieve non paged
 *  multiple people
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class PersonResponse(
    val persons: List<Person>? = null,
    val personError: PersonError? = null,
    val success: Boolean = false
) {

}
