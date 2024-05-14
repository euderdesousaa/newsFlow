package com.redue.newsflow.service;

import com.redue.newsflow.api.TheNewsApiTopData;
import com.redue.newsflow.api.responses.TheNewsApiData;
import com.redue.newsflow.repositories.UserLocationRepository;
import com.redue.newsflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TheNewsApiService {

    private static final Logger log = LoggerFactory.getLogger(TheNewsApiService.class);
    @Value("${thenewsapi.token}")
    private String token;

    private final RestTemplate restTemplate;

    private final UserRepository repositoryU;
    private final UserLocationRepository repositoryL;

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

    public Authentication filterByIsoCode(String isoCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        repositoryL.findUserByIsoCode(isoCode);
        return authentication;
    }

}