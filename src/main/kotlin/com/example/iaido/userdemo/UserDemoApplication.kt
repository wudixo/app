package com.example.iaido.userdemo

import com.example.iaido.userdemo.config.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig::class)
open class UserDemoApplication

fun main(args: Array<String>) {
	runApplication<UserDemoApplication>(*args)

}
