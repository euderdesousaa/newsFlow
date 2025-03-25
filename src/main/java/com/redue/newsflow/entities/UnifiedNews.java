package com.redue.newsflow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnifiedNews {
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String publishedAt;
    private String source;
}