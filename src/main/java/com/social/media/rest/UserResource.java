package com.social.media.rest;

import com.social.media.domain.model.User;
import com.social.media.domain.repository.UserRepository;
import com.social.media.rest.dto.CreateUserRequest;
import com.social.media.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest createUserRequest){
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        if(!violations.isEmpty()) {
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
        }

        User user = new User();
        user.setAge(createUserRequest.getAge());
        user.setName(createUserRequest.getName());

        userRepository.persist(user);
        return Response.
                status(Response.Status.CREATED).
                entity(user).
                build();
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
            return Response.noContent().build();
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
