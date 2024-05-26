package com.example.iaido.userdemo.actuator

import com.example.iaido.userdemo.model.entity.enum.AuthRoles
import com.example.iaido.userdemo.service.PersonService
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.stereotype.Component

@Component
@Endpoint(id = "person-numbers")
class PersonCheck(val personService: PersonService) {

    @ReadOperation
    fun getNumberOfPeople(): String {
        return "there are " + personService.getNumberOfPeople()
            .toString() + " people in the system of which " + personService.getNumberOfPeopleByRole(AuthRoles.ADMIN.name) +
                " are Admin and " + personService.getNumberOfPeopleByRole(AuthRoles.GUEST.name) + " are Guests";
    }
}

