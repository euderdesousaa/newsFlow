package com.redue.newsflow.api.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TheNewsApiData {

    private TheNewsApiMeta meta;

    private List<TheNewsApiResponse> data;

}
