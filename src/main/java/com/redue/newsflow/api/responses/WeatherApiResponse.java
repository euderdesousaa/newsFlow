package com.redue.newsflow.api.responses;

import lombok.Data;

@Data
public class WeatherApiResponse {
    private String cityName;
    private Weather current;
}
