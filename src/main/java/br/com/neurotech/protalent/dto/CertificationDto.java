package br.com.neurotech.protalent.dto;

import br.com.neurotech.protalent.model.Certification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record CertificationDto(
        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Institution is required")
        String institution,

        @NotBlank(message = "Date is required")
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$",
            message = "The date must be in the format dd/MM/yyyy")
        String date,

        @NotNull(message = "Hours is required")
        Integer hours
) {
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Certification toModel(CertificationDto dto) {
        return new Certification(
                dto.description,
                dto.institution,
                LocalDate.parse(dto.date, dtf),
                dto.hours()
        );
    }

    public CertificationDto(Certification certification) {
        this(
                certification.getDescription(),
                certification.getInstitution(),
                certification.getDate().format(dtf),
                certification.getHours()
        );
    }
}
