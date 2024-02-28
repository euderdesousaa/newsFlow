package com.redue.newsflow.entities;

import com.redue.newsflow.enums.Roles;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String name;
    private String email;
    private String password;
    private String country;
    private Roles roles;

}
