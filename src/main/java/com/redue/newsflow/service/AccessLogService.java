package com.redue.newsflow.service;

import com.redue.newsflow.entities.UserLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessLogService {

    
    private final MongoTemplate mongoTemplate;

    public void saveAccessLog(String userId, String ip, String country) {
        UserLocation log = new UserLocation();
        log.setUserId(userId);
        log.setIp(ip);
        log.setCountry(country);
        mongoTemplate.save(log);
    }
}
