package com.redue.newsflow.controller;

import com.redue.newsflow.api.TheNewsApiTopData;
import com.redue.newsflow.api.responses.NewsData;
import com.redue.newsflow.api.responses.TheNewsApiData;
import com.redue.newsflow.service.NewsApiService;
import com.redue.newsflow.service.TheNewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TheNewsApiController {

    private final TheNewsApiService service;

    private final NewsApiService nService;

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
    public TheNewsApiTopData getTopNews(@RequestParam(defaultValue = "1") int page
                                        ) {
        return service.findByTopNews(page);
    }

    @GetMapping("/search")
    public TheNewsApiData getNewsBySearch(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam String search) {
        return service.findBySearch(search, page);
    }

    @GetMapping("/another")
    public NewsData getAaNews(@RequestParam(defaultValue = "1") int page,
                              @RequestParam String search) {
        return nService.findByEverything(search, page);
    }

}
