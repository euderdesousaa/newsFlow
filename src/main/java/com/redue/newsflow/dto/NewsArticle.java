package com.redue.newsflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    private String title;
    private String link;
    private String thumbnail;
    private String source;
    private String description;
}
