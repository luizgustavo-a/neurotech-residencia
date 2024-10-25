package br.com.neurotech.protalent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EmployeeUpdateDto(
        @NotNull(message = "The id must be provided")
        Long id,

        @Size(min = 2,
                message = "The name must be at least 2 characters long")
        String name,

        @Email(message = "The email must be valid")
        String email,

        @Pattern(regexp = "(\\(?\\d{2}\\)?\\s?)(\\d{5}\\-?\\d{4})",
                message = "The contact must follow the (XX) XXXXX-XXXX pattern")
        String contact,

        List<TechnicalCompetenceDto> technicalCompetences,

        List<CertificationDto> certifications,

        Integer yearsOfExperience,

        @Pattern(regexp = "^https?:\\/\\/(www\\.)?linkedin\\.com\\/in\\/[a-zA-Z0-9-]{3,100}\\/?$",
                message = "The URL must be valid")
        String linkedinUrl
) {
}
