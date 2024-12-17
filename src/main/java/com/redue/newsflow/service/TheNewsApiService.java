package com.redue.newsflow.service;

import com.redue.newsflow.api.TheNewsApiTopData;
import com.redue.newsflow.api.responses.TheNewsApiData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TheNewsApiService {

    @Value("${thenewsapi.token}")
    private String token;

    private final RestTemplate restTemplate;

    public TheNewsApiData findAllNews(int page) {
        String finalApi = buildAPI("all", page);
        return restTemplate.getForObject(finalApi, TheNewsApiData.class);
    }

    public TheNewsApiData findByCategories(String categories, int page) {
        String finalApi = buildAPI("all", page)
                .concat("&categories=").concat(categories);
        return restTemplate.getForObject(finalApi, TheNewsApiData.class);
    }

    public TheNewsApiTopData findByTopNews(String language, String locale, int page) {
        String finalApi = buildAPI("top", page)
                .concat("&language=").concat(language)
                .concat("&locale=").concat(locale);
        return restTemplate.getForObject(finalApi, TheNewsApiTopData.class);
    }

    public TheNewsApiData findBySearch(String search, int page) {
        String finalApi = buildAPI("all", page)
                .concat("&search=").concat(search);
        return restTemplate.getForObject(finalApi, TheNewsApiData.class);
    }

    public String buildAPI(String endpoint, int page) {
        String url = "https://api.thenewsapi.com/v1/news/API_ENDPOINT?api_token=API_TOKEN";
        return url.replace("API_ENDPOINT", endpoint).replace("API_TOKEN", token)
                .concat("&page=").concat(String.valueOf(page));
    }
}