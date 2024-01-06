package com.social.media.quarkus.social.rest;

import com.social.media.quarkus.social.domain.model.Follower;
import com.social.media.quarkus.social.domain.repository.FollowerRepository;
import com.social.media.quarkus.social.domain.repository.UserRepository;
import com.social.media.quarkus.social.rest.dto.CreateFollowerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, CreateFollowerRequest followerRequest) {

        if(userId.equals(followerRequest.getFollowerId())) {
            return Response.status(Response.Status.CONFLICT).
                    entity("You can not follow yourself!")
                    .build();
        }

        var user = userRepository.findById(userId);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRepository.findById(followerRequest.getFollowerId());

        boolean follows = followerRepository.follows(follower, user);

        if(!follows) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            followerRepository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
