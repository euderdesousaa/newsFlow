package com.redue.newsflow.controller;

import com.redue.newsflow.api.TheNewsApiTopData;
import com.redue.newsflow.api.responses.TheNewsApiData;
import com.redue.newsflow.service.TheNewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news/")
@RequiredArgsConstructor
public class TheNewsApiController {

    private final TheNewsApiService service;

    @GetMapping
    public TheNewsApiData getTheNews(@RequestParam(defaultValue = "1") int page) {
        return service.findAllNews(page);
    }

    @GetMapping("{categories}")
    public TheNewsApiData getTheNewsByCategory(@PathVariable String categories,
                                               @RequestParam(defaultValue = "1") int page) {
        return service.findByCategories(categories, page);
    }

    @GetMapping("/top-news")
    public TheNewsApiTopData getTopNews(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "en") String language,
                                        @RequestParam(defaultValue = "us") String locale) {
        return service.findByTopNews(language ,locale, page);
    }
    @GetMapping("/query")
    public TheNewsApiData getNewsBySearch(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam String search) {
        return service.findBySearch(search, page);
    }

    @GetMapping("teste")
    public Authentication teste(String isoCode) {
        return service.filterByIsoCode(isoCode);
    }
}
