package com.redue.newsflow.repositories;

import com.redue.newsflow.entities.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLocationRepository extends MongoRepository<UserLocation, String> {

    Optional<UserLocation> findByUser_Id(String userId);

}
