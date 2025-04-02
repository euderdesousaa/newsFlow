package com.redue.newsflow.service;

import com.redue.newsflow.config.ScrappingConfig;
import com.redue.newsflow.dto.NewsArticle;
import com.redue.newsflow.dto.NewsDTO;
import com.redue.newsflow.dto.SiteConfig;
import com.redue.newsflow.utils.ThumbnailExtractor;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class NewsScrappingService {
    private static final Logger log = Logger.getLogger(NewsScrappingService.class.getName());
    
    public List<NewsDTO> fetchLatestNews() {
        List<NewsDTO> newsList = new ArrayList<>();

        for (String rssUrl : ScrappingConfig.ENGLISH_RSS_FEED) {
            try {
                URL url = new URL(rssUrl);
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));

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
            } catch (MalformedURLException e) {
                log.warning("URL inválida: " + rssUrl);
            } catch (FeedException | IOException e) {
            }
        }

        return newsList.stream()
                .filter(news -> news.getPublishedDate() != null)
                .sorted(Comparator.comparing(NewsDTO::getPublishedDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<NewsArticle> scrapeAllNews(String siteName, String category, String isoCode) {
        List<NewsArticle> allNews = new ArrayList<>();

        Object newsSources = ScrappingConfig.getNewsSources(category, isoCode);

        if (newsSources instanceof List<?>) {
            List<?> sources = (List<?>) newsSources;
            if (!sources.isEmpty()) {
                if (sources.get(0) instanceof String) {
                    List<String> rssFeeds = (List<String>) sources;
                    for (String feedUrl : rssFeeds) {
                        if (siteName == null || feedUrl.toLowerCase().contains(siteName.toLowerCase())) {
                            List<NewsArticle> rssNews = parseRssFeed(feedUrl);
                            if (!rssNews.isEmpty()) {
                                allNews.addAll(rssNews.subList(0, Math.min(rssNews.size(), ScrappingConfig.MAX_NEWS_PER_SITE)));
                            }
                        }
                    }
                } else if (sources.get(0) instanceof SiteConfig) {
                    List<SiteConfig> sites = (List<SiteConfig>) sources;
                    for (SiteConfig site : sites) {
                        if (siteName == null || site.getSiteName().equalsIgnoreCase(siteName)) {
                            List<NewsArticle> scrapedNews = scrapeNews(site);
                            if (!scrapedNews.isEmpty()) {
                                allNews.addAll(scrapedNews.subList(0, Math.min(scrapedNews.size(), ScrappingConfig.MAX_NEWS_PER_SITE)));
                            }
                        }
                    }
                }
            }
        }

        if (allNews.size() > 1) {
            Collections.shuffle(allNews);
        }
        return allNews;
    }

    private List<NewsArticle> parseRssFeed(String feedUrl) {
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
            String source = TitleTrimService.cleanTitle(feed.getTitle());
            return feed.getEntries().stream()
                    .map(entry -> {
                        String thumbnail = ThumbnailExtractor.extractThumbnail(entry);
                        return new NewsArticle(entry.getTitle(), entry.getLink(), thumbnail, source);
                    })
                    .toList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<NewsArticle> scrapeNews(SiteConfig site) {
        List<NewsArticle> articles = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(site.getUrl()).get();
            Elements titles = doc.select(site.getTitleSelector());
            Elements links = doc.select(site.getLinkSelector());
            Elements images = doc.select(site.getThumbnailSelector());

            int size = Math.min(titles.size(), Math.min(links.size(), images.size()));

            for (int i = 0; i < size; i++) {
                String title = titles.get(i).text();
                String link = links.get(i).attr("href");
                String image = images.get(i).attr("src");
                String source = site.getSiteName();

                if (!link.startsWith("http")) {
                    link = site.getUrl() + link;
                }

                articles.add(new NewsArticle(title, link, image, source));
            }
        } catch (IOException e) {
            log.throwing("Erro ao conectar no site {}: {}", site.getSiteName(), e);
        } catch (Exception e) {
            log.throwing("Erro inesperado ao coletar notícias de {}: {}", site.getSiteName(), e);
        }

        return articles;
    }
}
