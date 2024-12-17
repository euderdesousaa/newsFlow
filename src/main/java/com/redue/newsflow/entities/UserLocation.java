package com.redue.newsflow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {

    private String id;

    private String country;

    private boolean enabled;

    @DBRef
    private User user;

    private String isoCode;


}
