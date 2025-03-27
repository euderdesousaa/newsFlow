package com.redue.newsflow.controller;

import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.service.ScrappingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {


    private final ScrappingService scrappingService;

    @GetMapping
    public List<NewsDTO> fetchLastNews() {
        return scrappingService.fetchLatestNews();
    }

    @GetMapping("/all")
    public List<NewsArticle> getAllNews(@RequestParam(required = false) String source,
                                        @RequestParam(required = false) String category, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        System.out.println(userCountry);
        return scrappingService.scrapeAllNews(source, category, userCountry);
    }

    @GetMapping("/get-country")
    public ResponseEntity<String> getUserCountry(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        return ResponseEntity.ok(userCountry);
    }
}
