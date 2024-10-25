package br.com.neurotech.employees_management.dto;

import jakarta.validation.constraints.NotNull;

public record TechnicalCompetenceUpdateDto(
        @NotNull
        Long id,

        String description,

        String level
) {
}
