package com.redue.newsflow.service;

import com.redue.newsflow.api.responses.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherApiService {
    @Value("${weatherapi.token}")
    private String apiKey;
    private final RestTemplate restTemplate;

    public WeatherApiResponse getCurrentWeather(String ip) {
        String BASE_URL = "http://api.weatherapi.com/v1/current.json";
        String url = BASE_URL + "?key=" + apiKey + "&q=" + ip + "&aqi=no";

        WeatherApiResponse apiResponse = restTemplate.getForObject(url, WeatherApiResponse.class);

        if (apiResponse == null) {
            throw new RuntimeException("Não foi possível obter os dados do clima");
        }

        return apiResponse;
    }
}
