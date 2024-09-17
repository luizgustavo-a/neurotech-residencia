package br.com.neurotech.user_management.dto;

import br.com.neurotech.user_management.model.TechnicalCompetence;

;

public record TechnicalCompetenceDto(
        String description,
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
