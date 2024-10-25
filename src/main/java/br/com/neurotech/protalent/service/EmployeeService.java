package br.com.neurotech.protalent.service;

import br.com.neurotech.protalent.dto.CertificationDto;
import br.com.neurotech.protalent.dto.EmployeeCreationDto;
import br.com.neurotech.protalent.dto.EmployeeUpdateDto;
import br.com.neurotech.protalent.dto.TechnicalCompetenceDto;
import br.com.neurotech.protalent.model.Employee;
import br.com.neurotech.protalent.model.exception.EmployeeNotFoundException;
import br.com.neurotech.protalent.repository.CertificationRepository;
import br.com.neurotech.protalent.repository.EmployeeRepository;
import br.com.neurotech.protalent.repository.EmployeeSpecification;
import br.com.neurotech.protalent.repository.TechnicalCompetenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TechnicalCompetenceRepository technicalCompetenceRepository;
    private final CertificationRepository certificationRepository;

    public EmployeeService(EmployeeRepository employeeRepository, TechnicalCompetenceRepository technicalCompetenceRepository, CertificationRepository certificationRepository) {
        this.employeeRepository = employeeRepository;
        this.technicalCompetenceRepository = technicalCompetenceRepository;
        this.certificationRepository = certificationRepository;
    }

    public Employee createEmployee(EmployeeCreationDto creationDto) {
        Employee employee = new Employee(
                creationDto.name(),
                creationDto.email(),
                creationDto.contact(),
                creationDto.technicalCompetences()
                        .stream()
                        .map(TechnicalCompetenceDto::toModel)
                        .toList(),
                creationDto.certifications()
                        .stream()
                        .map(CertificationDto::toModel)
                        .toList(),
                creationDto.workExperience(),
                creationDto.linkedinUrl()
        );

        technicalCompetenceRepository.saveAll(employee.getTechnicalCompetences());
        certificationRepository.saveAll(employee.getCertifications());
        employeeRepository.save(employee);

        return employee;
    }

    public Page<Employee> listAllEmployeesWithFilters(List<String> technicalCompetences, List<String> certifications,
                                                      Integer yearsOfExperienceMin, Integer yearsOfExperienceMax, Pageable pageable) {
        return employeeRepository.findAll(
                EmployeeSpecification.filterUser(technicalCompetences,
                        certifications,
                        yearsOfExperienceMin,
                        yearsOfExperienceMax), pageable);
    }

    public Employee findEmployeeById(Long id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public Employee findEmployeeByEmail(String email) throws EmployeeNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public Employee updateEmployee(EmployeeUpdateDto updateDto) throws EmployeeNotFoundException {
        Employee employee = findEmployeeById(updateDto.id());

        if(updateDto.name() != null && !updateDto.name().isEmpty()) {
            employee.setName(updateDto.name());
        }
        if(updateDto.email() != null && !updateDto.email().isEmpty()) {
            employee.setEmail(updateDto.email());
        }
        if(updateDto.contact() != null && !updateDto.contact().isEmpty()) {
            employee.setContact(updateDto.contact());
        }
        if(updateDto.technicalCompetences() != null && !updateDto.technicalCompetences().isEmpty()) {
            employee.setTechnicalCompetences(updateDto.technicalCompetences()
                    .stream()
                    .map(TechnicalCompetenceDto::toModel)
                    .toList());
        }
        if(updateDto.certifications() != null && !updateDto.certifications().isEmpty()) {
            employee.setCertifications(updateDto.certifications()
                    .stream()
                    .map(CertificationDto::toModel)
                    .toList());
        }
        if(updateDto.yearsOfExperience() != null) {
            employee.setYearsOfExperience(updateDto.yearsOfExperience());
        }
        if(updateDto.linkedinUrl() != null && !updateDto.linkedinUrl().isEmpty()) {
            employee.setLinkedinUrl(updateDto.linkedinUrl());
        }

        employeeRepository.save(employee);

        return employee;
    }

    public Employee delete(Long id) throws EmployeeNotFoundException {
        Employee employee = findEmployeeById(id);

        employeeRepository.delete(employee);

        return employee;
    }
}
