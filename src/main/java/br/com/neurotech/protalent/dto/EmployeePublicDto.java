package br.com.neurotech.protalent.dto;

import br.com.neurotech.protalent.model.Employee;

import java.util.List;

public record EmployeePublicDto(
        Long id,
        String name,
        String email,
        String contact,
        List<TechnicalCompetenceDto> technicalCompetences,
        List<CertificationDto> certifications,
        Integer yearsOfExperience,
        String linkedinUrl
) {
    public EmployeePublicDto(Employee employee) {
        this(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getContact(),
                employee.getTechnicalCompetences()
                        .stream()
                        .map(TechnicalCompetenceDto::new)
                        .toList(),
                employee.getCertifications()
                        .stream()
                        .map(CertificationDto::new)
                        .toList(),
                employee.getYearsOfExperience(),
                employee.getLinkedinUrl()
        );
    }
}
