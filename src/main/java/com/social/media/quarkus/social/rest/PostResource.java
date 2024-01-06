package com.social.media.quarkus.social.rest;

import com.social.media.quarkus.social.domain.model.Post;
import com.social.media.quarkus.social.domain.model.User;
import com.social.media.quarkus.social.domain.repository.PostRepository;
import com.social.media.quarkus.social.domain.repository.UserRepository;
import com.social.media.quarkus.social.rest.dto.CreatePostRequest;
import com.social.media.quarkus.social.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {

        User user = userRepository.findById(userId);

        if(user == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(postRequest.getPostText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if(user == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        var postList = query.list();

        var postResponseList = postList.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok().entity(postResponseList).build();
    }

}
