package com.redue.newsflow.dto;

public record SignUpDto(String username,
                        String name,
                        String email,
                        String password,
                        String country) {
}
