package com.example.iaido.userdemo.model.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import com.example.iaido.userdemo.model.entity.Person
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthPerson(
    val id: String,
    private val username: String,
    val email: String,
    @JsonIgnore private val password: String,
    private val authorities: MutableCollection<out GrantedAuthority>

) : UserDetails {

    companion object {
        fun build(person: Person): UserDetails {
            val authority = SimpleGrantedAuthority(person.getRole().name);
            val mutableListOfRoles = mutableListOf(authority);
            return AuthPerson(person.id, person.username, person.email, person.password, mutableListOfRoles);
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities;
    }

    override fun getPassword(): String {
        return password;
    }

    override fun getUsername(): String {
        return username;
    }


    /**
     * return boolean is building basic security
     */

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true;
    }

    override fun isEnabled(): Boolean {
        return true;
    }
}
