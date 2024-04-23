package com.redue.newsflow.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String jwt) {
}
