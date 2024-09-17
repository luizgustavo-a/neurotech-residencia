package br.com.neurotech.user_management.dto;

import br.com.neurotech.user_management.model.User;

import java.util.List;

public record UserPublicDto(
        Long id,
        String name,
        String email,
        String contact,
        List<TechnicalCompetenceDto> technicalCompetences,
        List<CertificationDto> certifications,
        Integer yearsOfExperience,
        String linkedinUrl
) {
    public UserPublicDto(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getContact(),
                user.getTechnicalCompetences()
                        .stream()
                        .map(TechnicalCompetenceDto::new)
                        .toList(),
                user.getCertifications()
                        .stream()
                        .map(CertificationDto::new)
                        .toList(),
                user.getYearsOfExperience(),
                user.getLinkedinUrl()
        );
    }
}
