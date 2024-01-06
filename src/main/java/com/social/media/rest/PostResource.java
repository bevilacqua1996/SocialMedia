package com.social.media.rest;

import com.social.media.domain.model.User;
import com.social.media.domain.repository.FollowerRepository;
import com.social.media.domain.repository.PostRepository;
import com.social.media.domain.repository.UserRepository;
import com.social.media.domain.model.Post;
import com.social.media.rest.dto.CreatePostRequest;
import com.social.media.rest.dto.PostResponse;
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
    private final FollowerRepository followerRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {

        User user = userRepository.findById(userId);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(postRequest.getPostText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId,
                              @HeaderParam("followerId") Long followerId) {
        if(followerId==null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Follower Id is not present").build();
        }

        User user = userRepository.findById(userId);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerId);

        if(follower==null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Follower doesn't exist").build();
        }

        boolean follows = followerRepository.follows(follower, user);
        if(!follows) {
            return Response.status(Response.Status.FORBIDDEN).entity("You cant see this posts").build();
        }

        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        var postList = query.list();

        var postResponseList = postList.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok().entity(postResponseList).build();
    }

}
