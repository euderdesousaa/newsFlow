package com.redue.newsflow.api.responses;

import lombok.Data;

@Data
public class WeatherCondition {

    private String text;
    private String icon;

    private int code;
}
