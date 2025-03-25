package com.redue.newsflow.service;

import com.redue.newsflow.config.ScrappingConfig;
import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.dto.SiteConfig;
import com.redue.newsflow.utils.ThumbnailExtractor;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ScrappingService {
    private static final Logger log = Logger.getLogger(ScrappingService.class.getName());

    public List<NewsDTO> fetchLatestNews() {
        List<NewsDTO> newsList = new ArrayList<>();

        for (String rssUrl : ScrappingConfig.RSS_FEEDS) {
            try {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(rssUrl)));
                for (SyndEntry entry : feed.getEntries()) {
                    String thumbnail = ThumbnailExtractor.extractThumbnail(entry);

                    newsList.add(new NewsDTO(
                            entry.getTitle(),
                            entry.getLink(),
                            entry.getPublishedDate(),
                            feed.getTitle(),
                            thumbnail
                    ));
                }
            } catch (Exception e) {
                log.info("Failed to fetch: " + rssUrl);
            }
        }

        return newsList.stream()
                .filter(news -> news.getPublishedDate() != null)
                .sorted((a, b) -> b.getPublishedDate().compareTo(a.getPublishedDate()))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<NewsArticle> scrapeAllNews(String siteName, String category) {
        List<NewsArticle> allNews = new ArrayList<>();

        List<SiteConfig> sites = ScrappingConfig.getSitesByCategory(category);

        for (SiteConfig site : sites) {
            if (siteName == null || site.getSiteName().equalsIgnoreCase(siteName)) {
                List<NewsArticle> scrapedNews = scrapeNews(site);
                List<NewsArticle> limitedNews = scrapedNews.subList(0, Math.min(scrapedNews.size(), ScrappingConfig.MAX_NEWS_PER_SITE));
                allNews.addAll(limitedNews);
            }
        }

        Collections.shuffle(allNews);

        return allNews;
    }

    private List<NewsArticle> scrapeNews(SiteConfig site) {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(site.getUrl()).get();

            Elements titles = doc.select(site.getTitleSelector());
            Elements links = doc.select(site.getLinkSelector());
            Elements images = doc.select(site.getThumbnailSelector());

            int size = Math.min(titles.size(), Math.min(links.size(), images.size()));
            log.info("Total de notícias encontradas para " + site.getSiteName() + ": " + size);

            for (int i = 0; i < size; i++) {
                String title = titles.get(i).text();
                String link = links.get(i).attr("href");
                String image = images.get(i).attr("src");

                if (!link.startsWith("http")) {
                    link = site.getUrl() + link;
                }

                articles.add(new NewsArticle(title, link, image));
            }
        } catch (Exception e) {
            log.info("Erro ao coletar notícias de " + site.getSiteName() + ": " + e.getMessage());
        }

        return articles;
    }
}