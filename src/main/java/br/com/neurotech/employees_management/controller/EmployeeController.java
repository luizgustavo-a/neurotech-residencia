package br.com.neurotech.employees_management.controller;

import br.com.neurotech.employees_management.dto.EmployeeCreationDto;
import br.com.neurotech.employees_management.dto.EmployeePublicDto;
import br.com.neurotech.employees_management.dto.EmployeeUpdateDto;
import br.com.neurotech.employees_management.model.exception.EmployeeNotFoundException;
import br.com.neurotech.employees_management.service.EmployeeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeePublicDto> createEmployee(@RequestBody @Valid EmployeeCreationDto creationDto,
                                                        UriComponentsBuilder builder) {
        var employee = employeeService.createEmployee(creationDto);
        var uri = builder.path("/employee/{id}").buildAndExpand(employee.getId()).toUri();

        return ResponseEntity.created(uri).body(new EmployeePublicDto(employee));
    }

    @GetMapping
    public ResponseEntity<Page<EmployeePublicDto>> listAllEmployees(
            @RequestParam(value = "technical competence", required = false) List<String> technicalCompetences,
            @RequestParam(value = "certification", required = false) List<String> certifications,
            @RequestParam(value = "years of experience (min)", required = false) Integer yearsOfExperienceMin,
            @RequestParam(value = "years of experience (max)", required = false) Integer yearsOfExperienceMax,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        var paginatedEmployees = employeeService.listAllEmployeesWithFilters(technicalCompetences,
                certifications,
                yearsOfExperienceMin,
                yearsOfExperienceMax,
                pageable).map(EmployeePublicDto::new);

        return ResponseEntity.ok(paginatedEmployees);
    }

    @GetMapping("/{idOrEmail}")
    public ResponseEntity<EmployeePublicDto> findEmployee(@PathVariable String idOrEmail) throws EmployeeNotFoundException {
        try {
            Long id = Long.parseLong(idOrEmail);
            var employee = employeeService.findEmployeeById(id);
            return ResponseEntity.ok(new EmployeePublicDto(employee));
        } catch (Exception e) {
            // if the input is not the id, it is the email
        }
        var employee = employeeService.findEmployeeByEmail(idOrEmail);
        return ResponseEntity.ok(new EmployeePublicDto(employee));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<EmployeePublicDto> updateEmployee(@RequestBody @Valid EmployeeUpdateDto updateDto) throws EmployeeNotFoundException {
        var employee = employeeService.updateEmployee(updateDto);

        return ResponseEntity.ok(new EmployeePublicDto(employee));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<EmployeePublicDto> deleteEmployee(@PathVariable Long id) throws EmployeeNotFoundException {
        var employee = employeeService.delete(id);

        return ResponseEntity.ok(new EmployeePublicDto(employee));
    }
}
