package com.example.iaido.userdemo.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.example.iaido.userdemo.model.entity.enum.AuthRoles
import org.hibernate.annotations.GenericGenerator
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Indexed
@Entity(name = "persons")
class Person(
    @Field(name = "fullName")
    val forename: String,
    @Field(name = "fullName")
    val surname: String,
    val email: String, val phone: Long,
    val dateOfBirth: LocalDate, val username: String,
    @JsonIgnore val password: String, var age: Int = LocalDate.now().year - dateOfBirth.year,
) {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    val id: String = UUID.randomUUID().toString();

    @JsonIgnore
    var role: String = "";

    fun getRole(): AuthRoles {
        return AuthRoles.valueOf(role);
    }


}
