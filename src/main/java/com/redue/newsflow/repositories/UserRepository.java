package com.redue.newsflow.repositories;

import com.redue.newsflow.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
}
