package com.llwantedll.webhearts.models.repositories;

import com.llwantedll.webhearts.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByLogin(String login);

    User findByEmail(String email);

    long countByLogin(String login);
}
