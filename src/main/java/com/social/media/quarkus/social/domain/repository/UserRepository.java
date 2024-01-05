package com.social.media.quarkus.social.domain.repository;

import com.social.media.quarkus.social.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
