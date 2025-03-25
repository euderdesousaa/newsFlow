package com.redue.newsflow.controller;

import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.service.CustomRssService;
import com.redue.newsflow.service.ScrappingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final CustomRssService customRssService;

    private final ScrappingService scrappingService;

    @GetMapping
    public List<NewsDTO> fetchLastNews() {
        return scrappingService.fetchLatestNews();
    }

    @GetMapping("/all")
    public List<NewsArticle> getAllNews(@RequestParam(required = false) String source,
                                        @RequestParam(required = false) String category) {
        return scrappingService.scrapeAllNews(source, category);
    }

    @GetMapping("/g1")
    public List<NewsArticle> getG1News() {
        return customRssService.fetchG1News();
    }
    
    @GetMapping("myip")
    public String getMyIpNews(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return "My IP: " + ip;
    }

    @GetMapping("/user/ip")
    public String getUserIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
