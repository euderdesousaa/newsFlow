package com.redue.newsflow.controller;

import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.service.NewsScrappingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestController {
    
    private final NewsScrappingService scrappingService;

    @GetMapping
    public List<NewsDTO> fetchLastNews() {
        return scrappingService.fetchLatestNews();
    }

    @GetMapping("/all")
    public List<NewsArticle> getAllNews(@RequestParam(required = false) String source,
                                        @RequestParam(required = false) String category, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        return scrappingService.scrapeAllNews(source, category, userCountry);
    } 
    
    //TEST.
    @GetMapping("/get-country")
    public ResponseEntity<String> getUserCountry(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        return ResponseEntity.ok(userCountry);
    }
}
