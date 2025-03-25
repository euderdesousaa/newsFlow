package com.redue.newsflow.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TheNewsApiData {

    private TheNewsApiMeta meta;

    private List<TheNewsApiResponse> data;

}
