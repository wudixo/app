package com.example.iaido.userdemo.controller

import com.example.iaido.userdemo.AbstractIntegrationTest
import com.example.iaido.userdemo.model.dto.enum.PersonError
import com.example.iaido.userdemo.repo.PersonRepository
import com.example.iaido.userdemo.service.JwtService
import com.example.iaido.userdemo.util.RequestUtil
import org.assertj.core.api.Assertions
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql(scripts = ["/sql/controller/person-controller-cleanup.sql", "/sql/controller/person-controller-setup.sql"])
internal class PersonControllerIT : AbstractIntegrationTest() {

    @Autowired
    private lateinit var jwtService: JwtService;

    @Autowired
    private lateinit var personRepo: PersonRepository;

    @Test
    fun `getPerson guest role not authorized`() {
        val guestJwtToken = jwtService.generateJwtToken("guest.person");

        RequestUtil.get("v1/api/person/22c594f0-7b0b-11ec-90d6-0242ac120003", guestJwtToken)
            .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    fun `getPerson return person`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.get("v1/api/person/19526444-7b05-11ec-90d6-0242ac120435", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("persons[0].id", equalTo("19526444-7b05-11ec-90d6-0242ac120435"))
            .body("persons[0].forename", equalTo("tester"))
            .body("persons[0].surname", equalTo("two"))
            .body("persons[0].email", equalTo("tester.two@iaido.com"))
            .body("persons[0].phone", equalTo(7765477567))
            .body("persons[0].dateOfBirth", notNullValue())
            .body("persons[0].username", equalTo("guest.person"))
            .body("persons[0].age", equalTo(41))
            .body("success", equalTo(true))
        ;
    }

    @Test
    fun `getPerson no one found`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.get("v1/api/person/12", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("personError", equalTo(PersonError.NO_RESULTS_FOUND_FOR_ID.toString()))
            .body("success", equalTo(false))
        ;
    }

    @Test
    fun `deletePerson guest role not authorized`() {
        val guestJwtToken = jwtService.generateJwtToken("guest.person");

        RequestUtil.delete("v1/api/person/remove/cdc3a7f0-7b03-11ec-90d6-0242a3433s", guestJwtToken)
            .statusCode(HttpStatus.FORBIDDEN.value())
        ;
    }

    @Test
    fun `deletePerson`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.delete("v1/api/person/remove/19526444-7b05-11ec-90d6-0242ac120005", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("success", equalTo(true))
        ;
        var existsInDatabase = personRepo.existsById("19526444-7b05-11ec-90d6-0242ac120005");
        Assertions.assertThat(existsInDatabase).isFalse;
    }

    @Test
    fun `deletePerson no one found`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.delete("v1/api/person/remove/cdc3a7f0-7b03-11ec-90d6-0242a3433s", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("personError", equalTo(PersonError.NO_RESULTS_FOUND_FOR_ID.toString()))
            .body("success", equalTo(false))
        ;
    }

    @Test
    fun `upgrade person guest role not authorized`() {
        val guestJwtToken = jwtService.generateJwtToken("guest.person");

        RequestUtil.patch("v1/api/person/upgrade/cdc3a7f0-7b03-11ec-90d6-0242a3433s", guestJwtToken)
            .statusCode(HttpStatus.FORBIDDEN.value())
        ;
    }

    @Test
    fun `upgradePerson person not found`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.patch("v1/api/person/upgrade/cdc3a7efe-7b03-11ec-90d6-323sa3433s", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("success", equalTo(false))
            .body("personError", equalTo(PersonError.NO_RESULTS_FOUND_FOR_ID.toString()))
        ;
    }

    @Test
    fun `upgradePerson cant upgrade admin`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.patch("v1/api/person/upgrade/19526444-7b05-11ec-90d6-0242ac120653", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("success", equalTo(false))
            .body("personError", equalTo(PersonError.CANNOT_UPGRADE_PERSON_ALREADY_ADMIN.toString()))
        ;
    }

    @Test
    fun `upgradePerson upgrade guest`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.patch("v1/api/person/upgrade/19526444-7b05-11ec-90d6-0242ac120005", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("success", equalTo(true))
        ;
    }

    @Test
    fun `getAllPerson paged is guest not authorized`() {
        val guestJwtToken = jwtService.generateJwtToken("guest.person");

        RequestUtil.get("v1/api/person/all?page=0&size=2,asc", guestJwtToken)
            .statusCode(HttpStatus.FORBIDDEN.value())
        ;
    }

    @Test
    fun `getAllPerson paged sort by age`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.get("v1/api/person/all?page=0&size=2&sort=age", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("content[0].forename", equalTo("tester"))
            .body("content[0].surname", equalTo("two"))
            .body("content[0].email", equalTo("tester.two@iaido.com"))
            .body("content[0].username", equalTo("guest.person"))
            .body("content[0].phone", equalTo(7765477567))
            .body("content[0].age", equalTo(41))
            .body("content[0].id", equalTo("19526444-7b05-11ec-90d6-0242ac120435"))
            .body("content[1].forename", equalTo("tester"))
            .body("content[1].surname", equalTo("two"))
            .body("content[1].email", equalTo("tester.two@iaido.com"))
            .body("content[1].username", equalTo("tester.two"))
            .body("content[1].phone", equalTo(7765477567))
            .body("content[1].age", equalTo(44))
            .body("content[1].id", equalTo("19526444-7b05-11ec-90d6-0242ac120005"))
            .body("content[1].id", equalTo("19526444-7b05-11ec-90d6-0242ac120005"))
        ;

        RequestUtil.get("v1/api/person/all?page=1&size=2&sort=age", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("content[0].forename", equalTo("tester"))
            .body("content[0].surname", equalTo("one"))
            .body("content[0].email", equalTo("tester.one@iaido.com"))
            .body("content[0].username", equalTo("tester.one"))
            .body("content[0].phone", equalTo(7765477567))
            .body("content[0].age", equalTo(45))
            .body("content[0].id", equalTo("07df9d6a-79cc-11ec-90d6-0242ac120004"))
            .body("content[1].forename", equalTo("tester"))
            .body("content[1].surname", equalTo("two"))
            .body("content[1].email", equalTo("tester.two@iaido.com"))
            .body("content[1].username", equalTo("admin.person"))
            .body("content[1].phone", equalTo(7765477567))
            .body("content[1].age", equalTo(46))
            .body("content[1].id", equalTo("19526444-7b05-11ec-90d6-0242ac120653"))

        RequestUtil.get("v1/api/person/all?page=2&size=2&sort=age", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("empty", equalTo(true))
    }

    @Test
    fun `search default search parameters age OR partial surname return all results`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");
        RequestUtil.get("v1/api/person/shared/search?page=0&size=2&sort=age&forename=tes&age=44", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("content[0].forename", equalTo("tester"))
            .body("content[0].surname", equalTo("two"))
            .body("content[0].email", equalTo("tester.two@iaido.com"))
            .body("content[0].age", equalTo(41))
            .body("content[1].forename", equalTo("tester"))
            .body("content[1].surname", equalTo("two"))
            .body("content[1].email", equalTo("tester.two@iaido.com"))
            .body("content[1].age", equalTo(44))


        RequestUtil.get("v1/api/person/shared/search?page=1&size=2&sort=age&forename=tes&age=44", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("content[0].forename", equalTo("tester"))
            .body("content[0].surname", equalTo("one"))
            .body("content[0].email", equalTo("tester.one@iaido.com"))
            .body("content[0].age", equalTo(45))
            .body("content[1].forename", equalTo("tester"))
            .body("content[1].surname", equalTo("two"))
            .body("content[1].email", equalTo("tester.two@iaido.com"))
            .body("content[1].age", equalTo(46))

        RequestUtil.get("v1/api/person/shared/search?page=2&size=2&sort=age&forename=tes&age=44", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("empty", equalTo(true))
        ;
    }

    @Test
    fun `search partial email AND age`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");
        RequestUtil.get(
            "v1/api/person/shared/search?page=0&size=2&sort=age&searchType=AND&searchFields[]=AGE&searchFields[]=EMAIL&age=41&email=tester",
            adminJwtToken
        )
            .statusCode(HttpStatus.OK.value())
            .body("content[0].forename", equalTo("tester"))
            .body("content[0].surname", equalTo("two"))
            .body("content[0].email", equalTo("tester.two@iaido.com"))
            .body("content[0].age", equalTo(41))
            .body("totalElements", equalTo(1))


        RequestUtil.get(
            "v1/api/person/shared/search?page=1&size=2&sort=age&searchType=AND&searchFields[]=AGE&searchFields[]=EMAIL&age=41&email=tester",
            adminJwtToken
        )
            .statusCode(HttpStatus.OK.value())
            .body("empty", equalTo(true))
        ;
    }

    @Test
    fun `search partial email AND age made from guest account`() {
        val adminJwtToken = jwtService.generateJwtToken("guest.person");
        RequestUtil.get(
            "v1/api/person/shared/search?page=0&size=2&sort=age&searchType=AND&searchFields[]=AGE&searchFields[]=EMAIL&age=41&email=tester",
            adminJwtToken
        )
            .statusCode(HttpStatus.OK.value())
            .body("content[0].forename", equalTo("tester"))
            .body("content[0].surname", equalTo("two"))
            .body("content[0].email", equalTo("tester.two@iaido.com"))
            .body("content[0].age", equalTo(41))
            .body("totalElements", equalTo(1))


        RequestUtil.get(
            "v1/api/person/shared/search?page=1&size=2&sort=age&searchType=AND&searchFields[]=AGE&searchFields[]=EMAIL&age=41&email=tester",
            adminJwtToken
        )
            .statusCode(HttpStatus.OK.value())
            .body("empty", equalTo(true))
        ;
    }

    @Test
    fun `search no JWT not authorized`() {
        RequestUtil.getNoContentType("v1/api/person/shared/search?page=0&size=2&sort=age&searchType=AND&searchFields[]=AGE&searchFields[]=EMAIL&age=999&email=tester")
            .statusCode(HttpStatus.UNAUTHORIZED.value())
        ;
    }

    @Test
    fun `fuzzyFullNameSearch misspelt surname`() {
        val adminJwtToken = jwtService.generateJwtToken("admin.person");

        RequestUtil.get("v1/api/person/name-search?name=onp", adminJwtToken)
            .statusCode(HttpStatus.OK.value())
            .body("persons[0].id", equalTo("07df9d6a-79cc-11ec-90d6-0242ac120004"))
            .body("persons[0].forename", equalTo("tester"))
            .body("persons[0].surname", equalTo("one"))
            .body("persons[0].email", equalTo("tester.one@iaido.com"))
            .body("persons[0].phone", equalTo(7765477567))
            .body("persons[0].dateOfBirth", notNullValue())
            .body("persons[0].username", equalTo("tester.one"))
            .body("persons[0].age", equalTo(45))
            .body("success", equalTo(true))
        ;
    }

    @Test
    fun `fuzzyFullNameSearch guest not authorized`() {
        val adminJwtToken = jwtService.generateJwtToken("guest.person");

        RequestUtil.get("v1/api/person/name-search?name=onp", adminJwtToken)
            .statusCode(HttpStatus.FORBIDDEN.value())
        ;
    }

}
