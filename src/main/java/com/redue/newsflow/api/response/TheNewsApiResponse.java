package com.redue.newsflow.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TheNewsApiResponse {

    private String title;

    private String description;

    private String snippet;

    private String url;

    @JsonProperty("image_url")
    private String imageUrl;

    private String language;

    private String source;

    private List<String> categories;

    @JsonProperty("published_at")
    private String publishedAt;
}
