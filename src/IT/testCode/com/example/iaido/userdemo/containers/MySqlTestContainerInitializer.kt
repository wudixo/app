package com.example.iaido.userdemo.containers

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationEvent
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent
import shane.iaido.userdemo.containers.MySQLTestContainer

class MySqlTestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {

        val instance = MySQLTestContainer.getInstance();

        instance?.start()
        TestPropertyValues.of(
                 "spring.datasource.url=" + instance?.jdbcUrl,
                "spring.datasource.username=" + instance?.username,
                "spring.datasource.password=" + instance?.password
        ).applyTo(applicationContext.environment)
        applicationContext.addApplicationListener { c: ApplicationEvent ->
            if (c is ContextClosedEvent) {
                instance?.stop()
            }
        }
    }
}
