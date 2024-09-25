package br.com.neurotech.employees_management.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record EmployeeCreationDto(
        @NotBlank(message = "The name can not be blank")
        @Size(min = 2,
                message = "The name must be at least 2 characters long")
        String name,

        @NotBlank
        @Email(message = "The email must be valid")
        String email,

        @NotBlank
        @Pattern(regexp = "(\\(?\\d{2}\\)?\\s?)(\\d{5}\\-?\\d{4})",
                message = "The contact must follow the (XX) XXXXX-XXXX pattern")
        String contact,

        List<TechnicalCompetenceDto> technicalCompetences,

        List<CertificationDto> certifications,

        @NotNull(message = "If there is no work experience, write 0")
        @Min(value = 0)
        Integer workExperience,

        @NotBlank
        @Pattern(regexp = "^https?:\\/\\/(www\\.)?linkedin\\.com\\/in\\/[a-zA-Z0-9-]{3,100}\\/?$",
                message = "The URL must be valid")
        String linkedinUrl
) {
}
