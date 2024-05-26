package com.example.iaido.userdemo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig() {

    lateinit var secret: String;
    lateinit var expiration: String;

    /**
     * Not ideal but after a quick search couldn't find a way of having the late initialization
     * for a primitive type
     */
    fun getExirationInMs():Int{
        return expiration.toInt();
    }
}
