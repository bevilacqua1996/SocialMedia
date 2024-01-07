package com.social.media;

import com.social.media.domain.model.Follower;
import com.social.media.domain.model.User;
import com.social.media.domain.repository.FollowerRepository;
import com.social.media.domain.repository.UserRepository;
import com.social.media.rest.FollowerResource;
import com.social.media.rest.dto.CreateFollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
public class FollowerResourceTest {
    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    private Long userId;
    private Long userFollowerId;

    @BeforeEach
    @Transactional
    void setup() {
        var user = new User();

        user.setAge(32);
        user.setName("Abnaldo");

        userRepository.persist(user);
        userId = user.getId();

        var userFollower = new User();

        userFollower.setAge(21);
        userFollower.setName("Jonas");

        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setFollower(userFollower);
        follower.setUser(user);

        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("Should return 409 when followerId is same as userId")
    public void sameUserAsFollowerTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can not follow yourself!"));
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    public void userNotFoundWhenTryingToFollowTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", 999)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should follow User")
    public void followUserTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userFollowerId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    public void userNotFoundWhenListingFollowersTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", 999)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should list followers")
    public void listFollowersTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userFollowerId);

        var response = given()
                .contentType(ContentType.JSON)
                .body(followerRequest)
                .pathParam("userId", userId)
        .when()
                .get()
        .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followerCount");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(1, followersCount);
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    public void userNotFoundWhenUnfollowTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .queryParam("followerId", userFollowerId)
                .pathParam("userId", 999)
        .when()
                .delete()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should unfollow User")
    public void UnfollowUserTest() {
        CreateFollowerRequest followerRequest = new CreateFollowerRequest();
        followerRequest.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .queryParam("followerId", userFollowerId)
                .pathParam("userId", userId)
        .when()
                .delete()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}
