package com.redue.newsflow.service;

import com.redue.newsflow.api.responses.NewsApiData;
import com.redue.newsflow.api.responses.NewsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NewsApiService {

    @Value("${news.api.token}")
    private String token;

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://newsapi.org/v2";

    public NewsData findByEverything(String search, int page) {
        String finalApi = buildAPI("everything", page, null, null)
                .concat("&q=").concat(search)
                .concat("&sort=").concat("popularity");
        log.info("Fetching data from: " + finalApi);

        NewsData newsData = fetchData(finalApi, NewsData.class);

        if (newsData != null && newsData.getArticles() != null) {
            List<NewsApiData> filteredArticles = filterRemovedArticles(newsData.getArticles());
            newsData.setArticles(filteredArticles); // Atualizar os artigos filtrados
        }

        return newsData;
    }


    private String buildAPI(String endpoint, int page, String locale, String categories) {
        StringBuilder url = new StringBuilder(BASE_URL)
                .append("/")
                .append(endpoint)
                .append("?page=")
                .append(page)
                .append("&apiKey=")
                .append(token);


        if (locale != null) {
            url.append("&country=").append(locale);
        }

        if (categories != null) {
            url.append("&categories=").append(categories);
        }

        return url.toString();
    }

    private <T> T fetchData(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    public List<NewsApiData> filterRemovedArticles(List<NewsApiData> articles) {
        return articles.stream()
                .filter(article -> !containsRemovedText(article))
                .collect(Collectors.toList());
    }

    private boolean containsRemovedText(NewsApiData article) {
        return "[Removed]".equalsIgnoreCase(article.getTitle()) ||
                "[Removed]".equalsIgnoreCase(article.getDescription()) ||
                "[Removed]".equalsIgnoreCase(article.getContent());
    }

}