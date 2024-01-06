package com.social.media.quarkus.social.domain.repository;

import com.social.media.quarkus.social.domain.model.Follower;
import com.social.media.quarkus.social.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user) {
        var params = Parameters.with("follower", follower).
                and("user", user).map();

        PanacheQuery<Follower> followerPanacheQuery = find("follower = :follower and user = :user", params);
        Optional<Follower> firstResultOptional = followerPanacheQuery.firstResultOptional();

        return firstResultOptional.isPresent();
    }

}
