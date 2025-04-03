package com.redue.newsflow.service;

import com.redue.newsflow.dto.FootballTeamStanding;
import com.redue.newsflow.utils.SSLHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FootballScrappingService {
    public static List<FootballTeamStanding> scrapeBrasileiraoData(String serie) throws IOException {
        List<FootballTeamStanding> teams = new ArrayList<>();
        SSLHelper.disableSSLCertificateValidation();

        String url = brasileiraoUrls.getOrDefault(serie.toUpperCase(), brasileiraoUrls.get("A"));
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("table tbody tr");

        for (Element row : rows) {
            try {
                String positionText = row.select("td.styles_teamPosition__WLUWK strong.styles_position__O563L").first().text().trim();
                int position = Integer.parseInt(positionText.replaceAll("[^0-9]", ""));
                String teamName = row.select("td.styles_teamPosition__WLUWK a strong").text();
                int points = Integer.parseInt(row.select("td.styles_score__S1Ke3").first().text().trim());

                Elements stats = row.select("td");

                int gamesPlayed = Integer.parseInt(stats.get(2).text().trim());
                int wins = Integer.parseInt(stats.get(3).text().trim());
                int draws = Integer.parseInt(stats.get(4).text().trim());
                int losses = Integer.parseInt(stats.get(5).text().trim());
                int goalDifference = Integer.parseInt(stats.get(8).text().trim());

                teams.add(new FootballTeamStanding(teamName, position, gamesPlayed, wins, draws, losses, goalDifference, points));
            } catch (Exception e) {
                System.err.println("Erro ao processar linha: " + e.getMessage());
            }
        }

        return teams;
    }

    public List<FootballTeamStanding> scrapeBundesliga() {
        String url = "https://www.bundesliga.com/en/bundesliga/table";
        List<FootballTeamStanding> standings = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements rows = doc.select("table tbody tr");

            for (Element row : rows) {
                try {
                    int position = Integer.parseInt(row.select(".rank span").text());
                    String team = row.select(".team span.d-none.d-sm-inline-block").text();
                    int played = Integer.parseInt(row.select(".matches span").text());
                    int won = Integer.parseInt(row.select(".wins span").text());
                    int drawn = Integer.parseInt(row.select(".draws span").text());
                    int lost = Integer.parseInt(row.select(".losses span").text());

                    int goalDifference = Integer.parseInt(row.select(".difference span").text());
                    int points = Integer.parseInt(row.select(".pts span").text());

                    standings.add(new FootballTeamStanding(team, position, played, won, drawn, lost, goalDifference, points));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return standings;
    }

    public List<FootballTeamStanding> scrapeLaLiga() {
        String url = "https://www.bbc.com/sport/football/spanish-la-liga/table";
        List<FootballTeamStanding> standings = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();
            
            Elements rows = doc.select("table tbody tr");

            for (Element row : rows) {
                try {
                    String positionText = row.select(".ssrcss-owwpxq-Rank").text().trim();
                    int position = positionText.isEmpty() ? 0 : Integer.parseInt(positionText);

                    String team = row.select(".ssrcss-136gngg-TeamNameLink").text().trim();
                    int played = parseInteger(row.select("td:nth-of-type(2)").text());
                    int won = parseInteger(row.select("td:nth-of-type(3)").text());
                    int drawn = parseInteger(row.select("td:nth-of-type(4)").text());
                    int lost = parseInteger(row.select("td:nth-of-type(5)").text());
                    int goalDifference = parseInteger(row.select("td:nth-of-type(8)").text());
                    int points = parseInteger(row.select(".ssrcss-4tj2rv-Points").text());

                    standings.add(new FootballTeamStanding(team, position, played, won, drawn, lost, goalDifference, points));
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter um número: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return standings;
    }

    public List<FootballTeamStanding> scrapePremierLeague() {
        String url = "https://www.bbc.com/sport/football/premier-league/table";
        List<FootballTeamStanding> standings = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();

            Elements rows = doc.select("table tbody tr");

            for (Element row : rows) {
                try {
                    String positionText = row.select(".ssrcss-owwpxq-Rank").text().trim();
                    int position = positionText.isEmpty() ? 0 : Integer.parseInt(positionText);

                    String team = row.select(".ssrcss-136gngg-TeamNameLink").text().trim();

                    int played = parseInteger(row.select("td:nth-of-type(2)").text());
                    int won = parseInteger(row.select("td:nth-of-type(3)").text());
                    int drawn = parseInteger(row.select("td:nth-of-type(4)").text());
                    int lost = parseInteger(row.select("td:nth-of-type(5)").text());
                    int goalDifference = parseInteger(row.select("td:nth-of-type(8)").text());
                    int points = parseInteger(row.select(".ssrcss-4tj2rv-Points").text());

                    standings.add(new FootballTeamStanding(team, position, played, won, drawn, lost, goalDifference, points));
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter um número: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return standings;
    }

    private int parseInteger(String text) {
        text = text.trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    private static final Map<String, String> brasileiraoUrls = Map.of(
            "A", "https://www.cbf.com.br/futebol-brasileiro/tabelas/campeonato-brasileiro/serie-a/2025",
            "B", "https://www.cbf.com.br/futebol-brasileiro/tabelas/campeonato-brasileiro/serie-b/2025",
            "C", "https://www.cbf.com.br/futebol-brasileiro/tabelas/campeonato-brasileiro/serie-c/2025"
    );


}
