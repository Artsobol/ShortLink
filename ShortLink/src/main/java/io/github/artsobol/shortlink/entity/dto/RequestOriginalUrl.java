package io.github.artsobol.shortlink.entity.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestOriginalUrl (@NotBlank String originalUrl){}
