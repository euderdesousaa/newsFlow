package com.redue.newsflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocationDTO {
    private String ip_address;
    private String city;
    private String country;
    private String country_code;
}
