package com.redue.newsflow.repositories;

import com.redue.newsflow.entities.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRepository extends MongoRepository<UserLocation, String> {

    UserLocation findUserByIsoCode(String isoCode);
}
