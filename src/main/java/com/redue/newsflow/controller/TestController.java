package com.redue.newsflow.controller;

import com.redue.newsflow.dto.FootballTeamStanding;
import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.dto.BasketBallTeamStanding;
import com.redue.newsflow.entities.ConferenceStandings;
import com.redue.newsflow.service.BasketballScrappingService;
import com.redue.newsflow.service.FootballScrappingService;
import com.redue.newsflow.service.NewsScrappingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestController {

    private final NewsScrappingService scrappingService;

    private final BasketballScrappingService basketballScrappingService;

    private final FootballScrappingService footballScrappingService;


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
    public ResponseEntity<String> getUserCountry(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCountry = (String) session.getAttribute("userCountry");
        return ResponseEntity.ok(userCountry);
    }
}
