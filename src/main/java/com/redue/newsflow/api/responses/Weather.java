package com.redue.newsflow.api.responses;

import lombok.Data;

@Data
public class Weather {

    private Double temp_c;
    private Double temp_f;

    private int humidity;
    private WeatherCondition condition;
}
