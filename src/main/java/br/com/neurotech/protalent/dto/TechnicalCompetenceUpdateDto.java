package br.com.neurotech.protalent.dto;

import jakarta.validation.constraints.NotNull;

public record TechnicalCompetenceUpdateDto(
        @NotNull
        Long id,

        String description,

        String level
) {
}
