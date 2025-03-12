package com.redue.newsflow.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsData {
    private String status;
    private int totalResults;
    private List<NewsApiData> articles;
}
