package com.redue.newsflow.dto;

import lombok.*;

@Data
public class SignUpDto {
    private String username;
    private String name;
    private String email;
    private String password;
    private String country;
}
