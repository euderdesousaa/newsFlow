package com.redue.newsflow.controller;

import com.redue.newsflow.api.responses.Weather;
import com.redue.newsflow.api.responses.WeatherApiResponse;
import com.redue.newsflow.dto.FootballTeamStanding;
import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.dto.BasketBallTeamStanding;
import com.redue.newsflow.entities.ConferenceStandings;
import com.redue.newsflow.service.BasketballScrappingService;
import com.redue.newsflow.service.FootballScrappingService;
import com.redue.newsflow.service.NewsScrappingService;
import com.redue.newsflow.service.WeatherApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestController {

    private final NewsScrappingService scrappingService;

    private final BasketballScrappingService basketballScrappingService;

    private final FootballScrappingService footballScrappingService;
    
    private final WeatherApiService weatherApiService;


    @GetMapping
    public List<NewsDTO> fetchLastNews() {
        return scrappingService.fetchLatestNews();
    }

    @GetMapping("/all")
    public List<NewsArticle> getAllNews(@RequestParam(required = false) String source,
                                        @RequestParam(required = false) String category, HttpServletRequest request) {
        Map<String, String> sessionData = extractSession(request);
        return scrappingService.scrapeAllNews(source, category, sessionData);
    }
    
    @GetMapping("/weather")
    public WeatherApiResponse getWeather(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
       return weatherApiService.getCurrentWeather(ipAddress);
    }

    //Pensando se mantenho.
    @GetMapping("/standings")
    public ConferenceStandings getAllStandings() throws IOException {
        return basketballScrappingService.scrapeNbaStandings();
    }

    @GetMapping("/standings/conference")
    public List<BasketBallTeamStanding> getConferenceStandings(
            @RequestParam String conference) throws IOException {
        ConferenceStandings standings = basketballScrappingService.scrapeNbaStandings();

        if ("western".equalsIgnoreCase(conference)) {
            return standings.getWesternConference();
        } else if ("eastern".equalsIgnoreCase(conference)) {
            return standings.getEasternConference();
        } else {
            throw new IllegalArgumentException("Conference must be 'western' or 'eastern'");
        }
    }


    @GetMapping("/brasileirao")
    public List<FootballTeamStanding> getBrasileiraoData(@RequestParam(defaultValue = "A") String serie) throws IOException {
        return FootballScrappingService.scrapeBrasileiraoData(serie);
    }

    @GetMapping("/premier")
    public List<FootballTeamStanding> getPremierData() {
        return footballScrappingService.scrapePremierLeague();
    }

    @GetMapping("/bundesliga")
    public List<FootballTeamStanding> getBundesligaData() {
        return footballScrappingService.scrapeBundesliga();
    }

    @GetMapping("/laliga")
    public List<FootballTeamStanding> getLaligaData() throws IOException {
        return footballScrappingService.scrapeLaLiga();
    }

    //TEST.
    @GetMapping("/get-country")
    public ResponseEntity<Map<String, String>> getUserCountry(HttpServletRequest request) {
        Map<String, String> sessionData = extractSession(request);
        return ResponseEntity.ok(sessionData);
    }

    private Map<String, String> extractSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        String userCity = (String) session.getAttribute("userCity");
        Map<String, String> response = new HashMap<>();
        response.put("country", userCountry);
        response.put("city", userCity);
        return response;
    }
}
