package com.redue.newsflow.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {

    @Id
    private String id;
    @DBRef
    private User user;
    private String userId; // Pode ser nulo para visitantes
    private String ip;
    private String country;
    private String isoCode;

}
