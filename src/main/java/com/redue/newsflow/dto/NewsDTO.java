package com.redue.newsflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private String title;
    private String link;
    private Date publishedDate;
    private String source;
    private String thumbnail;

}
