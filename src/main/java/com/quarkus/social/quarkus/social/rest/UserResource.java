package com.quarkus.social.quarkus.social.rest;

import com.quarkus.social.quarkus.social.domain.model.User;
import com.quarkus.social.quarkus.social.domain.repository.UserRepository;
import com.quarkus.social.quarkus.social.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository userRepository;

    @Inject
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest createUserRequest){
        User user = new User();
        user.setAge(createUserRequest.getAge());
        user.setName(createUserRequest.getName());

        userRepository.persist(user);
        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> panacheQuery = userRepository.findAll();
        return Response.ok(panacheQuery.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user = userRepository.findById(id);

        if(user!=null) {
            userRepository.delete(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userRequest) {
        User user = userRepository.findById(id);

        if(user!=null) {
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            return Response.ok(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
