package br.com.neurotech.user_management.dto;

import br.com.neurotech.user_management.model.TechnicalCompetence;
import jakarta.validation.constraints.NotBlank;

public record TechnicalCompetenceDto(
        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Level is required")
        String level
) {
    public static TechnicalCompetence toModel(TechnicalCompetenceDto dto) {
        return new TechnicalCompetence(
                dto.description,
                dto.level
        );
    }

    public TechnicalCompetenceDto(TechnicalCompetence technicalCompetence) {
        this(
                technicalCompetence.getDescription(),
                technicalCompetence.getLevel()
        );
    }
}
