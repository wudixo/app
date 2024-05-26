package com.example.iaido.userdemo.model.entity

import com.example.iaido.userdemo.model.entity.enum.AuthRoles
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "roles")
class Role(@Id val id: Long, val name: String) {

    /**
     * Not ideal, would normally use @Enumerated
     */
    fun getAuthEnum():AuthRoles{
        return AuthRoles.valueOf(name);
    }

}
