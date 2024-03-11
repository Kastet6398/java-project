package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;


public record ContactResponse(
        @Schema(description = "contact") @jakarta.validation.constraints.NotBlank(message = "Id cannot be blank")
        Long contact,
        @Schema(description = "contact id")
        long id) {
}
