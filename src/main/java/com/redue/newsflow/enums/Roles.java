package com.redue.newsflow.enums;

import lombok.Getter;

@Getter
public enum Roles {
    OWNER("ROLE_OWNER"),
    USER("ROLE_USER");

    private final String role;

    Roles(String role) {
        this.role = role;
    }
}
