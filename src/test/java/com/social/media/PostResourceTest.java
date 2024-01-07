package com.social.media;

import com.social.media.domain.model.Follower;
import com.social.media.domain.model.Post;
import com.social.media.domain.model.User;
import com.social.media.domain.repository.FollowerRepository;
import com.social.media.domain.repository.PostRepository;
import com.social.media.domain.repository.UserRepository;
import com.social.media.rest.PostResource;
import com.social.media.rest.dto.CreatePostRequest;
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

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
public class PostResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;
    private Long userId;
    private Long userNotFollowerId;
    private Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp() {
        var user = new User();

        user.setAge(32);
        user.setName("Abnaldo");

        userRepository.persist(user);
        userId = user.getId();

        var userNotFollower = new User();

        userNotFollower.setAge(22);
        userNotFollower.setName("Abgail");

        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        var userFollower = new User();

        userFollower.setAge(21);
        userFollower.setName("Jonas");

        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setFollower(userFollower);
        follower.setUser(user);

        followerRepository.persist(follower);

        Post post = new Post();
        post.setText("WOW, it's a post!");
        post.setUser(user);

        postRepository.persist(post);
    }

    @Test
    @DisplayName("Should create a post for user")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setPostText("Test is done!");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
        .when()
                .post()
        .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

    }

    @Test
    @DisplayName("Should return 404 when trying to create post for a user which doesn't exists")
    public void postForAnNonexistentUserTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setPostText("Test is done!");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", 999)
        .when()
                .post()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("Should return 404 when user not found")
    public void listPostUserNotFoundTest() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", 999)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when header followerId is not present")
    public void listPostFollowerHeaderNotSendTest() {

        given()
            .contentType(ContentType.JSON)
            .pathParam("userId", userId)
        .when()
            .get()
        .then()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
            .body(Matchers.is("Follower Id is not present"));

    }

    @Test
    @DisplayName("Should return 400 when follower doesn't exist")
    public void listPostFollowerNotFoundTest() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .header("followerId", 999)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(Matchers.is("Follower doesn't exist"));
    }

    @Test
    @DisplayName("Should return 403 when follower doesn't follow")
    public void listPostNotAFollowerTest() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @DisplayName("Should return return posts")
    public void listPostsTest() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .header("followerId", userFollowerId)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", Matchers.is(1));
    }
}
