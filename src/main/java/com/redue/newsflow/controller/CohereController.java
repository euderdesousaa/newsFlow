package com.redue.newsflow.controller;

import com.redue.newsflow.service.CohereService;
import com.redue.newsflow.service.NewsScrappingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cohere")
public class CohereController {
    
    private final NewsScrappingService newsScrappingService;

    @GetMapping("/summarize")
    public ResponseEntity<String> generateSummary(
            @RequestParam(required = false) String siteName,
            @RequestParam(required = false) String category,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        try {
            String summary = newsScrappingService.generateSummaryFromScraping(siteName, category, userCountry);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao gerar resumo: " + e.getMessage());
        }
    }

}
