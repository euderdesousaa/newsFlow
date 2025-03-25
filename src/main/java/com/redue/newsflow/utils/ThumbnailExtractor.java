package com.redue.newsflow.utils;


import com.rometools.rome.feed.synd.SyndEntry;
import org.jdom2.Element;

public class ThumbnailExtractor {
    public static String extractThumbnail(SyndEntry entry) {
        for (Element element : entry.getForeignMarkup()) {
            if ("content".equals(element.getName()) && "media".equals(element.getNamespacePrefix())) {
                return element.getAttributeValue("url");
            }
            if ("thumbnail".equals(element.getName()) && "media".equals(element.getNamespacePrefix())) {
                return element.getAttributeValue("url");
            }
        }

        if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
            return entry.getEnclosures().get(0).getUrl();
        }

        return "https://via.placeholder.com/150";
    }
}