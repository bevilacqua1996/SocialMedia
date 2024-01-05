package com.quarkus.social.quarkus.social.domain.repository;

import com.quarkus.social.quarkus.social.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
