package com.example.iaido.userdemo


import com.example.iaido.userdemo.containers.MySqlTestContainerInitializer
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

@ActiveProfiles(profiles = ["it"])
@TestExecutionListeners(listeners = [SqlScriptsTestExecutionListener::class, DependencyInjectionTestExecutionListener::class])
@ContextConfiguration(initializers = [MySqlTestContainerInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTest {

    @LocalManagementPort
    protected var port = 0

    @BeforeEach
    fun before() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/"
    }

}
