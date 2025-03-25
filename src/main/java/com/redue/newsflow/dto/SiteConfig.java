package com.redue.newsflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiteConfig {
    private String siteName;
    private String url;
    private String titleSelector;
    private String linkSelector;
    private String thumbnailSelector;
}
