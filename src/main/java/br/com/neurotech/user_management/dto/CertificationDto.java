package br.com.neurotech.user_management.dto;

import br.com.neurotech.user_management.model.Certification;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record CertificationDto(
        String description,
        String institution,
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$",
            message = "The date must be in the format dd/MM/yyyy")
        String date
) {
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Certification toModel(CertificationDto dto) {
        return new Certification(
                dto.description,
                dto.institution,
                LocalDate.parse(dto.date, dtf)
        );
    }

    public CertificationDto(Certification certification) {
        this(
                certification.getDescription(),
                certification.getInstitution(),
                certification.getDate().format(dtf)
        );
    }
}
