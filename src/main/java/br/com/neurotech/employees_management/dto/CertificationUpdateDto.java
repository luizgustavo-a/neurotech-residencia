package br.com.neurotech.employees_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CertificationUpdateDto(
        @NotNull
        Long id,

        String description,

        String institution,

        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$",
            message = "The date must be in the format dd/MM/yyyy")
        String date,

        Integer hours
) {
}
