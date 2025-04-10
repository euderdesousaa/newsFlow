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
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsScrappingService {
    private static final Logger log = Logger.getLogger(NewsScrappingService.class.getName());

    private final CohereService cohereService;
    private static final int MAX_TEXT_LENGTH = 50000;
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
                log.warning("Erro ao processar feed: " + e.getMessage());
            }
        }

        return newsList.stream()
                .filter(news -> news.getPublishedDate() != null)
                .sorted(Comparator.comparing(NewsDTO::getPublishedDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<NewsArticle> scrapeAllNews(String siteName, String category, String userLocation) {
        List<NewsArticle> allNews = new ArrayList<>();

        Object newsSources = ScrappingConfig.getNewsSources(category, userLocation);

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

    public String generateSummaryFromScraping(String siteName, String category, String userLocation) {
        List<NewsArticle> newsArticles = scrapeAllNews(siteName, category, userLocation);
        StringBuilder combinedText = new StringBuilder();
        String idioma = getIdiomaByLocation(userLocation);

        for (NewsArticle article : newsArticles) {
            String titleLine = "Title: " + article.getTitle() + "\n";
            String descriptionLine = (article.getDescription() != null && !article.getDescription().isEmpty())
                    ? "Description: " + article.getDescription() + "\n"
                    : "";

            if (combinedText.length() + titleLine.length() + descriptionLine.length() + 1 > MAX_TEXT_LENGTH) {
                log.info("Limite de texto atingido, truncando em " + combinedText.length() + " caracteres.");
                break;
            }

            combinedText.append(titleLine);
            combinedText.append(descriptionLine);
            combinedText.append("\n");
        }

        if (combinedText.length() == 0) {
            return "No news available to summarize.";
        }

        log.info("Gerando resumo no idioma: " + idioma + " para localização: " + userLocation + " com " + combinedText.length() + " caracteres.");
        return cohereService.resumirTexto(combinedText.toString(), idioma);
    }

    private List<NewsArticle> parseRssFeed(String feedUrl) {
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
            String source = TitleTrimService.cleanTitle(feed.getTitle());
            return feed.getEntries().stream()
                    .map(entry -> {
                        String description = entry.getDescription() != null ? entry.getDescription().getValue() : "";
                        String thumbnail = ThumbnailExtractor.extractThumbnail(entry);
                        return new NewsArticle(entry.getTitle(), entry.getLink(), thumbnail, source, description);
                    })
                    .toList();
        } catch (Exception e) {
            log.warning("Erro ao processar RSS feed: " + e.getMessage());
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

                String content = scrapeArticleContent(link);
                articles.add(new NewsArticle(title, link, image, source, content));
            }
        } catch (IOException e) {
            log.warning("Erro ao conectar no site " + site.getSiteName() + ": " + e.getMessage());
        } catch (Exception e) {
            log.warning("Erro inesperado ao coletar notícias de " + site.getSiteName() + ": " + e.getMessage());
        }

        return articles;
    }

    private String scrapeArticleContent(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements paragraphs = doc.select("p");
            return paragraphs.text();
        } catch (IOException e) {
            log.warning("Erro ao extrair conteúdo de " + url + ": " + e.getMessage());
            return "";
        }
    }

    private String getIdiomaByLocation(String isoCode) {
        if (isoCode == null) {
            log.warning("isoCode está null, usando idioma padrão 'english'");
            return "english";
        } else if ("BR".equalsIgnoreCase(isoCode)) {
            return "português";
        } else if (ScrappingConfig.isFranceSource(isoCode)) {
            return "français";
        } else if (ScrappingConfig.isSpanishSource(isoCode)) {
            return "español";
        } else {
            log.info("Localização desconhecida: " + isoCode + ", usando 'english'");
            return "english";
        }
    }
}