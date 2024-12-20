package com.redue.newsflow.service;

import com.redue.newsflow.dto.GeoLocationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationService {

    private final RestTemplate restTemplate;

    @Value("${abstractapi.api_key}")
    private String apiKey;

    private static final String BASE_URL = "https://ipgeolocation.abstractapi.com/v1/";

    public GeoLocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoLocationDTO fetchGeoLocation(String ipAddress) {
        String url = BASE_URL + "?api_key=" + apiKey + "&ip_address=" + ipAddress;
        try {
            return restTemplate.getForObject(url, GeoLocationDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch geolocation data: " + e.getMessage(), e);
        }
    }
}
