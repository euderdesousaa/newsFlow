package com.redue.newsflow.api;

import com.redue.newsflow.api.responses.TheNewsApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheNewsApiTopResponse extends TheNewsApiResponse {

    private String locale;

}
