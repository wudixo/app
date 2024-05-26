package com.example.iaido.userdemo.repo

import com.example.iaido.userdemo.model.entity.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {

    fun findByUsername(username: String): Person

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun countAllByRole(role: String) : Long

}
