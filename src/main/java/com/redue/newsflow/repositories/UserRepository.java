package com.redue.newsflow.repositories;

import com.redue.newsflow.entities.User;
import com.redue.newsflow.entities.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsernameOrEmail(String username, String email);
    User findUserByLocation(UserLocation location, String username);
}
