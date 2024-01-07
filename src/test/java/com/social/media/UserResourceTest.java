package com.social.media;

import com.social.media.rest.dto.CreateUserRequest;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiUrl;

    @Test
    @DisplayName("Should create a user succesfully")
    @Order(1)
    public void createUserTest() {
        var user = new CreateUserRequest();
        user.setName("Abnaldo");
        user.setAge(33);

        var response = given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(apiUrl)
        .then()
            .extract()
            .response();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    public void createUserValidationTest() {
        var user = new CreateUserRequest();
        user.setAge(null);
        user.setName(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .post(apiUrl)
            .then()
                .extract()
                .response();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }

    @Test
    @DisplayName("Should list all Users")
    @Order(3)
    public void listAllUserTests() {

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(apiUrl)
        .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
}
