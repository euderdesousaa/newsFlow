package com.redue.newsflow.api.topresponses;

import com.redue.newsflow.api.response.TheNewsApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheNewsApiTopResponse extends TheNewsApiResponse {

    private String locale;
}
