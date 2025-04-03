package com.redue.newsflow.service;

import com.redue.newsflow.dto.BasketBallTeamStanding;
import com.redue.newsflow.entities.ConferenceStandings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BasketballScrappingService {

    public ConferenceStandings scrapeNbaStandings() throws IOException {
        String url = "https://www.basketball-reference.com/leagues/NBA_2025_standings.html";
        Document doc = Jsoup.connect(url).get();
        ConferenceStandings standings = new ConferenceStandings();

        Element westernTable = doc.select("table:contains(Western Conference)").first();
        standings.setWesternConference(parseConferenceTable(westernTable, "Western"));

        Element easternTable = doc.select("table:contains(Eastern Conference)").first();
        standings.setEasternConference(parseConferenceTable(easternTable, "Eastern"));

        return standings;
    }

    private List<BasketBallTeamStanding> parseConferenceTable(Element table, String conference) {
        List<BasketBallTeamStanding> teams = new ArrayList<>();
        Elements rows = table.select("tbody tr.full_table");

        for (Element row : rows) {
            BasketBallTeamStanding team = new BasketBallTeamStanding();

            team.setTeamName(row.select("th[data-stat=team_name] a").text());

            String seedText = row.select("span.seed").text().replaceAll("[()]", "");
            team.setPosition(Integer.parseInt(seedText));

            team.setWins(Integer.parseInt(row.select("td[data-stat=wins]").text()));
            team.setLosses(Integer.parseInt(row.select("td[data-stat=losses]").text()));

            teams.add(team);
        }
        return teams;
    }
}
