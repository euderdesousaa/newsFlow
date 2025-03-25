package com.redue.newsflow.config;

import com.redue.newsflow.dto.SiteConfig;

import java.util.List;

public class ScrappingConfig {
    public static final List<String> RSS_FEEDS = List.of(
            "https://feeds.bbci.co.uk/news/rss.xml",
            "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml",
            "https://edition.cnn.com/services/rss/",
            "https://www.theguardian.com/uk/rss",
            "https://www.forbes.com/most-popular/feed/",
            "https://feeds.bbci.co.uk/sport/rss.xml"
    );

    public static final int MAX_NEWS_PER_SITE = 10;

    public static List<SiteConfig> getSitesByCategory(String category) {
        if ("esportes".equalsIgnoreCase(category)) {
            return List.of(
                    new SiteConfig("Metropole", "https://www.metropoles.com/esportes", ".noticia__titulo a", ".noticia__titulo a", ".bloco-noticia__figure-imagem"),
                    new SiteConfig("R7", "https://esportes.r7.com", "h3 a span", "h3 a", "figure a img"),
                    new SiteConfig("G1", "https://ge.globo.com", ".feed-post-body-title a", ".feed-post-body-title a", ".bstn-fd-picture-image"),
                    new SiteConfig("CNN-Brasil", "https://www.cnnbrasil.com.br/esportes/", "h3.block__news__title", "a.block--manchetes__image__encapsulator", "img.block--manchetes__image")
            );
        } else if ("ultimas-noticias".equalsIgnoreCase(category)) {
            return List.of(
                    new SiteConfig("Metropole", "https://www.metropoles.com/ultimas-noticias", ".noticia__titulo a", ".noticia__titulo a", ".bloco-noticia__figure-imagem"),
                    new SiteConfig("G1", "https://g1.globo.com/ultimas-noticias/", ".feed-post-body-title a", ".feed-post-body-title a", ".bstn-fd-picture-image"),
                    new SiteConfig("CNN-Brasil", "https://www.cnnbrasil.com.br/ultimas-noticias/", "h3.block__news__title", "a.block--manchetes__image__encapsulator", "img.block--manchetes__image")
            );
        } else {
            return List.of(
                    new SiteConfig("Metropole", "https://www.metropoles.com", ".noticia__titulo a", ".noticia__titulo a", ".bloco-noticia__figure-imagem"),
                    new SiteConfig("R7", "https://www.r7.com", "h3 a span", "h3 a", "figure a img"),
                    new SiteConfig("G1", "https://g1.globo.com", ".feed-post-body-title a", ".feed-post-body-title a", ".bstn-fd-picture-image"),
                    new SiteConfig("CNN-Brasil", "https://www.cnnbrasil.com.br", "h3.block__news__title", "a.block--manchetes__image__encapsulator", "img.block--manchetes__image")
            );
        }
    }
}