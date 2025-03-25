package com.redue.newsflow.controller;

import com.redue.newsflow.api.TheNewsApiTopData;
import com.redue.newsflow.api.responses.NewsData;
import com.redue.newsflow.api.responses.TheNewsApiData;
import com.redue.newsflow.entities.UnifiedNews;
import com.redue.newsflow.service.NewsApiService;
import com.redue.newsflow.service.TheNewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public NewsData getAnotherNews(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam String search) {
        return nService.findByEverything(search, page);
    }

    @GetMapping("/combined-news")
    public List<UnifiedNews> getCombinedNews(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(required = false) String search) {
        TheNewsApiData theNewsApiData = service.findAllNews(page);
        NewsData newsApiData = nService.findByEverything(search, page);

        List<UnifiedNews> theNewsList = theNewsApiData.getData().stream().map(article ->
                new UnifiedNews(
                        article.getTitle(),
                        article.getSnippet(),
                        article.getUrl(),
                        article.getImageUrl(),
                        article.getPublishedAt(),
                        article.getSource()
                )
        ).collect(Collectors.toList());

        List<UnifiedNews> newsApiList = newsApiData.getArticles().stream().map(article ->
                new UnifiedNews(
                        article.getTitle(),
                        article.getDescription(),
                        article.getUrl(),
                        article.getUrlToImage(),
                        article.getPublishedAt(),
                        article.getSource().getName()
                )
        ).collect(Collectors.toList());

        List<UnifiedNews> combinedNews = new ArrayList<>();
        combinedNews.addAll(theNewsList);
        combinedNews.addAll(newsApiList);

        combinedNews.sort((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt()));

        return combinedNews;
    }


}
