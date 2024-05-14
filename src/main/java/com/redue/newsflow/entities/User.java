package com.redue.newsflow.entities;

import com.redue.newsflow.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;

    private String name;

    @Email
    private String email;

    @Size(max = 120)
    private String password;

    private Roles roles;

    @DBRef
    private UserLocation location;

}
