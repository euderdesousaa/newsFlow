package com.redue.newsflow.config;

import com.redue.newsflow.dto.SiteConfig;

import java.util.List;
import java.util.Locale;

public class ScrappingConfig {
    public static final int MAX_NEWS_PER_SITE = 10;

    public static final List<String> INTERNATIONAL_RSS_FEEDS = List.of(
            "https://feeds.bbci.co.uk/news/rss.xml",
            "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml",
            "https://edition.cnn.com/services/rss/",
            "https://www.theguardian.com/uk/rss",
            "https://www.forbes.com/most-popular/feed/",
            "https://feeds.bbci.co.uk/sport/rss.xml"
    );

    public static final List<String> SPANISH_RSS_FEEDS = List.of(
            "https://www.eldiario.es/rss", // Spain 
            "https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml", //Spain
            "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", // Spain
            "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/section/america/portada", // America Latina
            "https://www.clarin.com/rss/lo-ultimo/", //America Latina(AR)
            "https://feeds.bbci.co.uk/sport/rss.xml" //
    );



    // Cached configurations
    private static final List<SiteConfig> DEFAULT_BR_SITES = List.of(
            new SiteConfig("Metropole", "https://www.metropoles.com", ".noticia__titulo a", ".noticia__titulo a", ".bloco-noticia__figure-imagem"),
            new SiteConfig("R7", "https://www.r7.com", "h3 a span", "h3 a", "figure a img"),
            new SiteConfig("G1", "https://g1.globo.com", ".feed-post-body-title a", ".feed-post-body-title a", ".bstn-fd-picture-image"),
            new SiteConfig("CNN-Brasil", "https://www.cnnbrasil.com.br", "h3.block__news__title", "a.block--manchetes__image__encapsulator", "img.block--manchetes__image")
    );

    private static final List<SiteConfig> BR_SPORTS_SITES = List.of(
            new SiteConfig("Metropole", "https://www.metropoles.com/esportes", ".noticia__titulo a", ".noticia__titulo a", ".bloco-noticia__figure-imagem"),
            new SiteConfig("R7", "https://esportes.r7.com", "h3 a span", "h3 a", "figure a img"),
            new SiteConfig("G1", "https://ge.globo.com", ".feed-post-body-title a", ".feed-post-body-title a", ".bstn-fd-picture-image"),
            new SiteConfig("CNN-Brasil", "https://www.cnnbrasil.com.br/esportes/", "h3.block__news__title", "a.block--manchetes__image__encapsulator", "img.block--manchetes__image")
    );

    private static final List<SiteConfig> BR_NEWS_SITES = List.of(
            new SiteConfig("Metropole", "https://www.metropoles.com/ultimas-noticias", ".noticia__titulo a", ".noticia__titulo a", ".bloco-noticia__figure-imagem"),
            new SiteConfig("G1", "https://g1.globo.com/ultimas-noticias/", ".feed-post-body-title a", ".feed-post-body-title a", ".bstn-fd-picture-image"),
            new SiteConfig("CNN-Brasil", "https://www.cnnbrasil.com.br/ultimas-noticias/", "h3.block__news__title", "a.block--manchetes__image__encapsulator", "img.block--manchetes__image")
    );


    public static Object getNewsSources(String category, String isoCode) {
        if (isInternationalSource(isoCode)) {
            return INTERNATIONAL_RSS_FEEDS;
        }

        if ("BR".equalsIgnoreCase(isoCode)) {
            return getBrazilianSites(category);
        }

        return INTERNATIONAL_RSS_FEEDS; // Default fallback to RSS
    }

    private static boolean isInternationalSource(String isoCode) {
        return isUnknownCountry(isoCode) || "US".equalsIgnoreCase(isoCode);
    }

    private static boolean isUnknownCountry(String isoCode) {
        return isoCode == null || "Desconhecido".equalsIgnoreCase(isoCode);
    }

    private static List<SiteConfig> getBrazilianSites(String category) {
        if (category == null) {
            return DEFAULT_BR_SITES;
        }

        return switch (category.toLowerCase(Locale.ROOT)) {
            case "esportes" -> BR_SPORTS_SITES;
            case "ultimas-noticias" -> BR_NEWS_SITES;
            default -> DEFAULT_BR_SITES;
        };
    }
}