package com.redue.newsflow.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TitleTrimService {
    private static final List<String> UNWANTED_PHRASES = List.of(
            "Most Popular on ",
            " > Top Stories"
    );

    private static final Pattern PATTERN = Pattern.compile("(?i)(" + String.join("|", UNWANTED_PHRASES) + ")");

    public static String cleanTitle(String title) {
        if (title == null || title.isBlank()) {
            return title;
        }
        return PATTERN.matcher(title).replaceAll("").trim();
    }
}
